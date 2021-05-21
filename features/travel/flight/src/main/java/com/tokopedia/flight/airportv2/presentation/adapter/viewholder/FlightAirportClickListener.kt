package com.tokopedia.flight.airportv2.presentation.adapter.viewholder

import com.tokopedia.flight.airportv2.presentation.model.FlightAirportModel

interface FlightAirportClickListener {
    fun airportClicked(airportViewModel: FlightAirportModel)
    fun getFilterText(): String
}