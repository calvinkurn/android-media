package com.tokopedia.flight.orderlist.view.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 10/07/2020
 */
@Parcelize
data class FlightCancellationJourney(
        var journeyId: String = "",
        var departureCity: String = "",
        var departureCityCode: String = "",
        var departureAirportId: String = "",
        var departureTime: String = "",
        var arrivalCity: String = "",
        var arrivalCityCode: String = "",
        var arrivalAirportId: String = "",
        var arrivalTime: String = "",
        var airlineName: String = "",
        var isRefundable: Boolean = false,
        var airlineIds: List<String> = arrayListOf()
) : Parcelable