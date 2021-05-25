package com.tokopedia.flight.booking.data

import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityMetaModel
import java.util.*

/**
 * @author by jessica on 2019-10-29
 */

data class FlightCartViewEntity(
        var journeySummaries: List<JourneySummary> = listOf(),
        var insurances: List<FlightCart.Insurance> = listOf(),
        var luggageModels: ArrayList<FlightBookingAmenityMetaModel> = arrayListOf(),
        var mealModels: ArrayList<FlightBookingAmenityMetaModel> = arrayListOf(),
        var orderDueTimeStamp: Date = Date(),
        var isRefreshCart: Boolean = false
) {
    data class JourneySummary(
            var journeyId: String = "",
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