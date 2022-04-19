package com.tokopedia.flight.homepage.data.cache

/**
 * @author by jessica on 22/10/20
 */

object FlightDashboardGQLQueries {
    val FLIGHT_FARE_CALENDAR_QUERY = """
        query flightFareCalendar(${'$'}departCode: String!, ${'$'}arrivalCode: String!, ${'$'}year: String!, ${'$'}class: String){
          flightFare(departCode:${'$'}departCode, arrivalCode:${'$'}arrivalCode, year:${'$'}year, class: ${'$'}class) {
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