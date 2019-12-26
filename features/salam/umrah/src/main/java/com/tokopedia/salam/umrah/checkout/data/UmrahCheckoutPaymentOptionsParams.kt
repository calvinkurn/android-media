package com.tokopedia.salam.umrah.checkout.data

/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutPaymentOptionsParams (
        val price: Int = 0,
        val pilgrims: Int = 1,
        val departureDate : String = "",
        val downPayment: Int = 0
)