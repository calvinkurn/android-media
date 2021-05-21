package com.tokopedia.flight.dummy

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.airportv2.presentation.model.FlightAirportModel

/**
 * @author by furqan on 22/06/2020
 */

val DUMMY_POPULAR_AIRPORT = arrayListOf<Visitable<*>>(
        FlightAirportModel().apply {
            airportName = "Soekarno Hatta Intl. Airport"
            countryName = "Indonesia"
            airportCode = "CGK"
            cityCode = ""
            cityId = ""
            cityName = "Jakarta"
            cityAirports = arrayListOf()
        },
        FlightAirportModel().apply {
            airportName = "Halim Perdana Kusuma Airport"
            countryName = "Indonesia"
            airportCode = "HLP"
            cityCode = ""
            cityId = ""
            cityName = "Jakarta"
            cityAirports = arrayListOf()
        },
        FlightAirportModel().apply {
            airportName = ""
            countryName = "Indonesia"
            airportCode = ""
            cityCode = "JKTA"
            cityId = ""
            cityName = "Jakarta"
            cityAirports = arrayListOf("CGK", "HLP")
        },
        FlightAirportModel().apply {
            airportName = "Sultan Iskandar Muda Intl. Airport"
            countryName = "Indonesia"
            airportCode = "BTJ"
            cityCode = ""
            cityId = ""
            cityName = "Banda Aceh"
            cityAirports = arrayListOf()
        }
)

val DUMMY_SUGGESTION_AIRPORT = arrayListOf<Visitable<*>>(
        FlightAirportModel().apply {
            airportName = "Sultan Iskandar Muda Intl. Airport"
            countryName = "Indonesia"
            airportCode = "BTJ"
            cityCode = ""
            cityId = ""
            cityName = "Banda Aceh"
            cityAirports = arrayListOf()
        })