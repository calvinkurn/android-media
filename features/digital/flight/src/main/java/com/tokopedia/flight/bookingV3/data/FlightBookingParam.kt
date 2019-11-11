package com.tokopedia.flight.bookingV3.data

/**
 * @author by jessica on 2019-11-05
 */

data class FlightBookingParam (
        var isDomestic: Boolean = true,
        var insurances: MutableList<FlightCart.Insurance> = arrayListOf()
)