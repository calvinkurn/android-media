package com.tokopedia.flight.detail.view.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderTypeFactory
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailRouteViewModel

/**
 * @author by furqan on 03/10/2019
 */
class FlightDetailOrderJourney(
        journeyId: Long,
        departureCity: String,
        departureCityCode: String,
        departureAiportId: String,
        departureTime: String,
        arrivalCity: String,
        arrivalCityCode: String,
        arrivalAirportId: String,
        arrivalTime: String,
        status: String,
        routeViewModels: List<FlightOrderDetailRouteViewModel>)
    : FlightOrderJourney(
        journeyId,
        departureCity,
        departureCityCode,
        departureAiportId,
        departureTime,
        arrivalCity,
        arrivalCityCode,
        arrivalAirportId,
        arrivalTime,
        status,
        routeViewModels), Visitable<FlightDetailOrderTypeFactory>, Parcelable {

    override fun type(typeFactory: FlightDetailOrderTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}