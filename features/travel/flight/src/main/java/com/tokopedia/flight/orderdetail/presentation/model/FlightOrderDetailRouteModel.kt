package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationDetailRouteTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailRouteModel(
        val departureId: String,
        val departureTime: String,
        val departureAirportName: String,
        val departureCityName: String,
        val arrivalId: String,
        val arrivalTime: String,
        val arrivalAirportName: String,
        val arrivalCityName: String,
        val pnr: String,
        val airlineId: String,
        val airlineName: String,
        val airlineLogo: String,
        val operatorAirlineId: String,
        val flightNumber: String,
        val duration: String,
        val durationMinute: Int,
        val layover: String,
        val layoverMinute: Int,
        val refundable: Boolean,
        val departureTerminal: String,
        val arrivalTerminal: String,
        val stop: Int,
        val carrier: String,
        val stopDetails: List<String>,
        val ticketNumbers: List<OrderDetailTicketNumberModel>,
        val freeAmenities: FlightOrderDetailFreeAmenityModel
) : Parcelable, Visitable<FlightOrderCancellationDetailRouteTypeFactory> {

    @Parcelize
    data class OrderDetailTicketNumberModel(
            val passengerId: Int,
            val ticketNumber: String
    ) : Parcelable

    override fun type(typeFactory: FlightOrderCancellationDetailRouteTypeFactory): Int =
            typeFactory.type(this)

}