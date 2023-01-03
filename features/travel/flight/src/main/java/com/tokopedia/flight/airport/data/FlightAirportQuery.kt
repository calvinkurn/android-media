package com.tokopedia.flight.airport.data

import com.tokopedia.flight.airport.data.QueryAirPortPopularCity.QUERY_AIRPORT_POPULAR_CITY
import com.tokopedia.flight.airport.data.QueryAirPortSuggestion.QUERY_AIRPORT_SUGGESTION
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * @author by furqan on 08/10/2020
 */
@GqlQuery("AirPortPopularCityQuery", QUERY_AIRPORT_POPULAR_CITY)
internal object QueryAirPortPopularCity {
    const val QUERY_AIRPORT_POPULAR_CITY = """
        {
            flightPopularCity{
                countryID
                countryName
                cityID
                airportCode
                cityCode
                cityName
                airportName
                isPopular
            }
        }
    """
}

@GqlQuery("AirPortSuggestionQuery", QUERY_AIRPORT_SUGGESTION)
internal object QueryAirPortSuggestion {
    const val QUERY_AIRPORT_SUGGESTION = """
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
    """
}
