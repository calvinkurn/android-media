package com.tokopedia.flight.detail.view.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.detail.view.adapter.FlightDetailRouteTypeFactory
import com.tokopedia.flight.orderlist.view.viewmodel.FlightStopOverViewModel
import com.tokopedia.flight.search.data.cloud.single.Amenity
import kotlinx.android.parcel.Parcelize

/**
 * @author alvarisi
 */
@Parcelize
data class FlightDetailRouteModel(
        var pnr: String = "",
        var airlineName: String = "",
        var airlineCode: String = "",
        var airlineLogo: String = "",
        var flightNumber: String = "",
        var departureTimestamp: String = "",
        var departureAirportCity: String = "",
        var departureAirportCode: String = "",
        var departureAirportName: String = "",
        var departureTerminal: String = "",
        var isRefundable: Boolean = false,
        var duration: String = "",
        var arrivalTimestamp: String = "",
        var arrivalAirportCity: String = "",
        var arrivalAirportCode: String = "",
        var arrivalAirportName: String = "",
        var arrivalTerminal: String = "",
        var layover: String = "",
        var stopOver: Int = 0,
        var infos: List<FlightDetailRouteInfoModel> = arrayListOf(),
        var amenities: List<Amenity> = arrayListOf(),
        var stopOverDetail: List<FlightStopOverViewModel> = arrayListOf(),
        var operatingAirline: String = ""
) : Parcelable, Visitable<FlightDetailRouteTypeFactory> {

    override fun type(typeFactory: FlightDetailRouteTypeFactory): Int {
        return typeFactory.type(this)
    }

}