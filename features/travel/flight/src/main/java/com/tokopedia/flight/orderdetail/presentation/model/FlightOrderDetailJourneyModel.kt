package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailJourneyTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailJourneyModel(
        val id: Int,
        val status: Int,
        val departureId: String,
        val departureTime: String,
        val departureAirportName: String,
        val departureCityName: String,
        val arrivalId: String,
        val arrivalTime: String,
        val arrivalAirportName: String,
        val arrivalCityName: String,
        val totalTransit: Int,
        val totalStop: Int,
        val addDayArrival: Int,
        val duration: String,
        val durationMinute: Int,
        val fare: FlightOrderDetailFareModel,
        val routes: List<FlightOrderDetailRouteModel>,
        val webCheckIn: FlightOrderDetailWebCheckInModel,
        var airlineLogo: String? = null,
        var airlineName: String = "",
        var refundableInfo: Boolean = false,
        var departureDateAndTime: Pair<String, String> = Pair("", "")
) : Parcelable, Visitable<FlightOrderCancellationDetailJourneyTypeFactory> {

    override fun type(journeyTypeFactory: FlightOrderCancellationDetailJourneyTypeFactory): Int =
            journeyTypeFactory.type(this)

}