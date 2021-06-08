package com.tokopedia.flight.airport.presentation.adapter.viewholder

import com.tokopedia.flight.airport.presentation.model.FlightAirportModel

interface FlightAirportClickListener {
    fun airportClicked(airportViewModel: FlightAirportModel)
    fun getFilterText(): String
}