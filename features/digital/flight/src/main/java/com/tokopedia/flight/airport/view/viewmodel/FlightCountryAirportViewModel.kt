package com.tokopedia.flight.airport.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.airport.view.adapter.FlightAirportAdapterTypeFactory

class FlightCountryAirportViewModel(
        val countryId: String,
        val countryName: String,
        val airports: List<FlightAirportViewModel>
) : Visitable<FlightAirportAdapterTypeFactory> {

    override fun type(typeFactory: FlightAirportAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
