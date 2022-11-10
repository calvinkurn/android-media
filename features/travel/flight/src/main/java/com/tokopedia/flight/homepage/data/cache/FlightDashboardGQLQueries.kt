package com.tokopedia.flight.homepage.data.cache

import com.tokopedia.flight.homepage.data.cache.FlightDashboardGQLQueries.FLIGHT_FARE_CALENDAR_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * @author by jessica on 22/10/20
 */

@GqlQuery("QueryFlightFareCalendar", FLIGHT_FARE_CALENDAR_QUERY)
internal object FlightDashboardGQLQueries {
    const val FLIGHT_FARE_CALENDAR_QUERY = """
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
    """
}