package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class OrderDetailJourneyModel(
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
        val fare: OrderDetailFareModel,
        val routes: List<OrderDetailRouteModel>,
        val webCheckIn: OrderDetailWebCheckInModel
) : Parcelable