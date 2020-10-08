package com.tokopedia.flight.airportv2.data

/**
 * @author by furqan on 08/10/2020
 */
object FlightAirportQuery {
    val QUERY_AIRPORT_POPULAR_CITY = """
        {
            flightPopularCity{
                countryID
                countryName
                cityID
                airportCode
                cityCode
                cityName
                airportName
            }
        }
    """.trimIndent()
}