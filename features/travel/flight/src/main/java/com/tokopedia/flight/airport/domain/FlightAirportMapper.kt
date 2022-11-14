package com.tokopedia.flight.airport.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.airport.data.source.entity.FlightAirportSuggestionEntity
import com.tokopedia.flight.airport.data.source.entity.FlightPopularCityEntity
import com.tokopedia.flight.airport.presentation.model.FlightAirport
import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import com.tokopedia.flight.airport.presentation.model.FlightCountryAirportModel
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by nabillasabbaha on 05/03/19.
 */
class FlightAirportMapper @Inject constructor(
    @Named(NAMED_POPULAR_AIRPORT)
    private val popularAirportName: String
) {

    companion object {
        private const val POPULAR_AIRPORT = "POPULAR"
        const val NAMED_POPULAR_AIRPORT = "popular_airport"
    }

    fun groupingPopularCity(entities: List<FlightPopularCityEntity>): MutableMap<String, List<FlightAirport>> {
        val listCountry = mutableMapOf<String, List<FlightAirport>>()

        listCountry[POPULAR_AIRPORT] = getPopularAirport(entities)

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
            val listAirports = listCountry[it.countryId] as List<FlightAirport>
            val listNewAirport = mutableListOf<FlightAirport>()
            listNewAirport.addAll(listAirports)
            listNewAirport.add(flightAirport)
            listCountry.set(it.countryId, listNewAirport)
        }
        return listCountry
    }

    private fun getPopularAirport(entities: List<FlightPopularCityEntity>): List<FlightAirport> {
        val listPopularAirports = mutableListOf<FlightAirport>()
        entities.forEach {
            if (it.isPopular) {
                listPopularAirports.add(
                    FlightAirport(
                        it.countryId,
                        it.countryName,
                        it.cityId,
                        it.airportCode,
                        it.cityCode,
                        it.cityName,
                        it.airportName
                    )
                )
            }
        }
        return listPopularAirports
    }

    fun groupingSuggestion(entities: List<FlightAirportSuggestionEntity>): MutableMap<String, List<FlightAirport>> {
        val listCountry = mutableMapOf<String, List<FlightAirport>>()
        val flightAirportinOneCountry = mutableListOf<FlightAirport>()

        entities.map { itemSuggestion ->
            var airportArray = mutableListOf<String>()

            flightAirportinOneCountry.clear()

            var index = 0
            itemSuggestion.airports.map {
                airportArray.add(it.id)
                index++
            }

            if (itemSuggestion.airports.isEmpty()) {
                flightAirportinOneCountry.add(
                    FlightAirport(
                        itemSuggestion.countryId,
                        itemSuggestion.countryName[0].value,
                        itemSuggestion.code,
                        itemSuggestion.code,
                        itemSuggestion.code,
                        itemSuggestion.cityName[0].value,
                        itemSuggestion.name[0].value
                    )
                )
            } else {
                flightAirportinOneCountry.add(
                    FlightAirport(
                        itemSuggestion.countryId,
                        itemSuggestion.countryName[0].value,
                        itemSuggestion.code,
                        "",
                        itemSuggestion.code,
                        itemSuggestion.cityName[0].value,
                        itemSuggestion.name[0].value,
                        airportArray
                    )
                )
            }

            itemSuggestion.airports.map {
                flightAirportinOneCountry.add(
                    FlightAirport(
                        itemSuggestion.countryId,
                        itemSuggestion.countryName[0].value,
                        itemSuggestion.code,
                        it.id,
                        itemSuggestion.code,
                        itemSuggestion.cityName[0].value,
                        it.name[0].value
                    )
                )
            }

            if (!listCountry.containsKey(itemSuggestion.countryId)) {
                listCountry.put(itemSuggestion.countryId, mutableListOf())
            }
            val listAirports = listCountry[itemSuggestion.countryId] as List<FlightAirport>
            val listNewAirport = mutableListOf<FlightAirport>()
            listNewAirport.addAll(listAirports)
            listNewAirport.addAll(flightAirportinOneCountry)
            listCountry.set(itemSuggestion.countryId, listNewAirport)
        }
        return listCountry
    }

    fun transformToVisitable(mapAirport: MutableMap<String, List<FlightAirport>>): List<Visitable<*>> {
        val visitables: ArrayList<Visitable<*>> = ArrayList()

        mapAirport.map {
            val flightAirportList: List<FlightAirport> = it.value

            if (it.key == POPULAR_AIRPORT) {
                val popularAirport = FlightCountryAirportModel(
                    POPULAR_AIRPORT,
                    popularAirportName,
                    mutableListOf()
                )
                visitables.add(popularAirport)
            } else {
                val countryAirportViewModel = FlightCountryAirportModel(
                    flightAirportList[0].countryId,
                    flightAirportList[0].countryName,
                    mutableListOf()
                )
                visitables.add(countryAirportViewModel)
            }

            flightAirportList.map { airport ->
                val airportViewModel = FlightAirportModel()
                airportViewModel.airportName = airport.airportName
                airportViewModel.countryName = airport.countryName
                airportViewModel.airportCode = airport.airportCode
                airportViewModel.cityCode = airport.cityCode
                airportViewModel.cityId = airport.cityId
                airportViewModel.cityName = airport.cityName
                airportViewModel.cityAirports = airport.airports
                return@map visitables.add(airportViewModel)
            }
        }
        return visitables
    }
}
