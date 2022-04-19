package com.tokopedia.flight.airport.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.airport.presentation.adapter.FlightAirportAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightCountryAirportModel(
        val countryId: String,
        val countryName: String,
        val airports: List<FlightAirportModel>
) : Visitable<FlightAirportAdapterTypeFactory>, Parcelable {

    override fun type(typeFactory: FlightAirportAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
