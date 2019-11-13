package com.tokopedia.flight.bookingV3.data

import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel

/**
 * @author by jessica on 2019-11-05
 */

data class FlightBookingParam (
        var departureId: String = "",
        var returnId: String = "",
        var cartId: String = "",
        var searchParam: FlightSearchPassDataViewModel = FlightSearchPassDataViewModel(),
        var insurances: MutableList<FlightCart.Insurance> = arrayListOf()
)