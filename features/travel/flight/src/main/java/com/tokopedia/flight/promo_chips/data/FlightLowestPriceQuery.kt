package com.tokopedia.flight.promo_chips.data

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
        """.trimIndent()
}