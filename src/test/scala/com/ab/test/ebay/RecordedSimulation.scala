package com.ab.test.ebay

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://www.ebay.co.uk")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en;q=0.8,de;q=0.5,ru;q=0.3")
		.doNotTrackHeader("1")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:47.0) Gecko/20100101 Firefox/47.0")

  val searchTerms:Array[String] = Array("iPhone", "Samsung Galaxy", "Sony Xperia")
  val user1 = scenario("User search" + searchTerms(0)).exec(new Search(searchTerms(0)).search)
	setUp(
    scenario("User search" + searchTerms(0)).exec(new Search(searchTerms(0)).search).inject(atOnceUsers(1)),
    scenario("User search" + searchTerms(1)).exec(new Search(searchTerms(1)).search).inject(atOnceUsers(1)),
    scenario("User search" + searchTerms(2)).exec(new Search(searchTerms(2)).search).inject(atOnceUsers(1)))
    .protocols(httpProtocol)

}
class Search(searchTerm: String) {
  val search = exec(http("load ebay homepage")
    .get("/")
    .headers(Map("Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")))
    .pause(1)
    .exec(http("search")
      .get("/sch/i.html?_from=R40&_trksid=p2050601.m570.l1313.TR12.TRC2.A0.H0.X"+searchTerm+".TRS0&_nkw="+searchTerm+"&_sacat=0")
      .headers(Map("Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")))
    .pause(2)
}
