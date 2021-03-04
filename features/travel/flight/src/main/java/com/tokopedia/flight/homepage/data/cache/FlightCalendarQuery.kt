package com.tokopedia.flight.homepage.data.cache

/**
 * @author by furqan on 08/10/2020
 */
object FlightCalendarQuery {
    val QUERY_CALENDAR_FARE = """
        query flightFareCalendar(${'$'}departCode: String!,${'$'}arrivalCode: String!,${'$'}year: String!, ${'$'}class: String){
          flightFare(departCode:${'$'}departCode,arrivalCode:${'$'}arrivalCode,year:${'$'}year,class:${'$'}class) {
            id
            attributes {
              date
              cheapestPriceNumeric
              cheapestPrice
              displayedFare
              isLowestFare
            }
          }
        }

    """.trimIndent()
}