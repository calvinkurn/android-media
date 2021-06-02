package com.tokopedia.flight.airport.data

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

    val QUERY_AIRPORT_SUGGESTION = """
        query flightSuggestion(${'$'}input:String!) {
            flightSuggestion(input:${'$'}input) {
                suggestions {
                    airports {
                        id
                        name {
                          key
                          value
                        }
                    }
                    cityName {
                        key
                        value
                    }
                    cityID
                    code
                    countryID
                    countryName {
                        key
                        value
                    }
                    name {
                        key
                        value
                    }
                }
        	}
        }
    """.trimIndent()
}