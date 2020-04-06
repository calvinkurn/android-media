package com.tokopedia.flight.airport.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.airport.view.adapter.FlightAirportAdapterTypeFactory

class FlightCountryAirportModel(
        val countryId: String,
        val countryName: String,
        val airports: List<FlightAirportModel>
) : Visitable<FlightAirportAdapterTypeFactory> {

    override fun type(typeFactory: FlightAirportAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
