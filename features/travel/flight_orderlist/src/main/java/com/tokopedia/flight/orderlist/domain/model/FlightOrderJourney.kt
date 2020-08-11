package com.tokopedia.flight.orderlist.domain.model

import android.os.Parcelable
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailRouteViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
open class FlightOrderJourney(
        val journeyId: Long,
        val departureCity: String,
        val departureCityCode: String,
        val departureAiportId: String,
        val departureTime: String,
        val arrivalCity: String,
        val arrivalCityCode: String,
        val arrivalAirportId: String,
        val arrivalTime: String,
        val status: String,
        val routeViewModels: List<FlightOrderDetailRouteViewModel>)
    : Parcelable
