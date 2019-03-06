package com.tokopedia.flight.airport.domain

import com.tokopedia.flight.airport.data.source.cloud.model.FlightPopularCityEntity
import com.tokopedia.flight.airport.domain.model.FlightAirport
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 05/03/19.
 */
class FlightAirportMapper @Inject constructor() {

    fun groupingCountry(entities: List<FlightPopularCityEntity>): MutableMap<String, List<FlightAirport>> {
        val listCountry = mutableMapOf<String, List<FlightAirport>>()

        entities.map {
            val flightAirport = FlightAirport(
                    it.countryId,
                    it.countryName,
                    it.cityId,
                    it.airportCode,
                    it.cityCode,
                    it.cityName,
                    it.airportName
            )
            if (!listCountry.containsKey(it.countryId)) {
                listCountry.put(it.countryId, mutableListOf())
            }
            val listAirports = listCountry.get(it.countryId) as List<FlightAirport>
            val listNewAirport = mutableListOf<FlightAirport>()
            listNewAirport.addAll(listAirports)
            listNewAirport.add(flightAirport)
            listCountry.set(it.countryId, listNewAirport)
        }
        return listCountry
    }
}
