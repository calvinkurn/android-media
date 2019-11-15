package com.tokopedia.flight.bookingV3.data

import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel

/**
 * @author by jessica on 2019-11-05
 */

data class FlightBookingModel (
        var departureId: String = "",
        var returnId: String = "",
        var cartId: String = "",
        var isDomestic: Boolean = true,
        var isMandatoryDob: Boolean = false,
        var flightPriceViewModel: FlightPriceViewModel = FlightPriceViewModel(),
        var searchParam: FlightSearchPassDataViewModel = FlightSearchPassDataViewModel(),
        var insurances: MutableList<FlightCart.Insurance> = arrayListOf()
)