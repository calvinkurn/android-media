package com.tokopedia.flight.airportv2.presentation.model

/**
 * Created by nabillasabbaha on 05/03/19.
 */
class FlightAirport(
        val countryId: String,
        val countryName: String,
        val cityId: String,
        val airportCode: String,
        val cityCode: String,
        val cityName: String,
        val airportName: String,
        val airports: MutableList<String> = arrayListOf())
