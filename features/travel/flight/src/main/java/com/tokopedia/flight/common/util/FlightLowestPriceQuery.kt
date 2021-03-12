package com.tokopedia.flight.common.util

/**
 * author by astidhiyaa on 12/03/2021
 */

object FlightLowestPriceQuery {
    val flightLowestPriceInput = """
        query FlightLowestPrice(${'$'}data: FlightLowestPriceArgs!) {
	        flightLowestPrice(input: ${'$'}data) {
		        data {
			    date
			    airlinePrices {
                    airlineID
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
        """.trimIndent()
}