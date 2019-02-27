package com.tokopedia.flight.orderlist.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderTypeFactory
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel

class FlightOrderJourney(
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
        val routeViewModels: List<FlightDetailRouteViewModel>)
    : Visitable<FlightDetailOrderTypeFactory>, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(FlightDetailRouteViewModel.CREATOR)) {
    }

    override fun type(typeFactory: FlightDetailOrderTypeFactory?): Int {
        return typeFactory!!.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(journeyId)
        parcel.writeString(departureCity)
        parcel.writeString(departureCityCode)
        parcel.writeString(departureAiportId)
        parcel.writeString(departureTime)
        parcel.writeString(arrivalCity)
        parcel.writeString(arrivalCityCode)
        parcel.writeString(arrivalAirportId)
        parcel.writeString(arrivalTime)
        parcel.writeString(status)
        parcel.writeTypedList(routeViewModels)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlightOrderJourney> {
        override fun createFromParcel(parcel: Parcel): FlightOrderJourney {
            return FlightOrderJourney(parcel)
        }

        override fun newArray(size: Int): Array<FlightOrderJourney?> {
            return arrayOfNulls(size)
        }
    }

}
