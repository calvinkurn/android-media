package com.tokopedia.flight.airportv2.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.airportv2.presentation.adapter.FlightAirportAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightAirportModel(
        var countryName: String = "",
        var cityId: String = "",
        var cityName: String = "",
        var cityCode: String = "",
        var airportName: String = "",
        var airportCode: String = "",
        var cityAirports: MutableList<String> = arrayListOf()
) : Visitable<FlightAirportAdapterTypeFactory>, Parcelable {

    override fun type(typeFactory: FlightAirportAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}