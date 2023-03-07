package com.tokopedia.flight.promo_chips.data

import com.tokopedia.flight.promo_chips.data.FlightLowestPriceQuery.QUERY_FLIGHT_LOWEST_PRICE
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * author by astidhiyaa on 12/03/2021
 */

@GqlQuery("QueryFlightLowestPrice", QUERY_FLIGHT_LOWEST_PRICE)
object FlightLowestPriceQuery {
    const val QUERY_FLIGHT_LOWEST_PRICE = """
       query FlightLowestPrice(${'$'}data: FlightLowestPriceArgs!) {
	        flightLowestPrice(input: ${'$'}data) {
		        data {
			    date
			    airlinePrices {
                    airlineID
                    airlineName
                    logo
                    price
                    priceNumeric
			    }
		       }
		        error {
			    id
			    status
			    title
		        }
	            }
        }
        """
}