package com.tokopedia.flight.bookingV3.data

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel

/**
 * @author by jessica on 2019-10-29
 */

data class FlightCartViewEntity(
        var journeySummaries: List<JourneySummary> = listOf(),
        var insurances: List<FlightCart.Insurance> = listOf(),
        var luggageModels: List<FlightBookingAmenityMetaViewModel> = listOf(),
        var mealModels: List<FlightBookingAmenityMetaViewModel> = listOf()
) {
    data class JourneySummary(
            var airlineLogo: String = "",
            var airline: String = "",
            var routeName: String = "",
            var date: String = "",
            var isRefundable: Boolean = false,
            var transit: Int = 0,
            var journeyDetailUrl: String = "",
            var isMultipleAirline: Boolean = false
    )
}