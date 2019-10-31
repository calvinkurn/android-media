package com.tokopedia.flight.bookingV3.data

/**
 * @author by jessica on 2019-10-29
 */

data class FlightCartViewEntity(
        var journeySummaries: List<JourneySummary> = listOf()
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