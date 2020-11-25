package com.tokopedia.flight.searchV4.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.flight.searchV4.data.FlightSearchRepository
import com.tokopedia.flight.searchV4.data.cache.db.JourneyAndRoutes
import com.tokopedia.flight.searchV4.data.cloud.single.Amenity
import com.tokopedia.flight.searchV4.data.cloud.single.Info
import com.tokopedia.flight.searchV4.data.cloud.single.Route
import com.tokopedia.flight.searchV4.data.cloud.single.StopDetailEntity
import com.tokopedia.flight.searchV4.presentation.model.FlightFareModel
import com.tokopedia.flight.searchV4.presentation.model.FlightJourneyModel
import javax.inject.Inject

/**
 * @author by furqan on 15/04/2020
 */
class FlightSearchJouneyByIdUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) {

    suspend fun execute(journeyId: String): FlightJourneyModel =
            mapToFlightJourneyViewModel(flightSearchRepository.getJourneyById(journeyId))

    private fun mapToFlightJourneyViewModel(journeyAndRoutes: JourneyAndRoutes): FlightJourneyModel {
        val gson = Gson()
        val routes = journeyAndRoutes.routes.map {
            val stopDetailJsonString = it.stopDetail
            val stopDetailType = object : TypeToken<List<StopDetailEntity>>() {}.type
            val stopDetails = gson.fromJson<List<StopDetailEntity>>(stopDetailJsonString, stopDetailType)

            val amenitiesJsonString = it.amenities
            val amenitiesType = object : TypeToken<List<Amenity>>() {}.type
            val amenities = gson.fromJson<List<Amenity>>(amenitiesJsonString, amenitiesType)

            val infosJsonString = it.infos
            val infosType = object : TypeToken<List<Info>>() {}.type
            val infos = gson.fromJson<List<Info>>(infosJsonString, infosType)

            Route(
                    it.airline,
                    it.departureAirport,
                    it.departureTimestamp,
                    it.arrivalAirport,
                    it.arrivalTimestamp,
                    it.duration,
                    it.layover,
                    infos,
                    it.flightNumber,
                    it.isRefundable,
                    amenities,
                    it.stops,
                    stopDetails,
                    it.airlineName,
                    it.airlineLogo,
                    it.departureAirportName,
                    it.departureAirportCity,
                    it.arrivalAirportName,
                    it.arrivalAirportCity,
                    it.operatingAirline
            )
        }
        with(journeyAndRoutes.flightJourneyTable) {
            val fare = FlightFareModel(
                    adult,
                    adultCombo,
                    child,
                    childCombo,
                    infant,
                    infantCombo,
                    adultNumeric,
                    adultNumericCombo,
                    childNumeric,
                    childNumericCombo,
                    infantNumeric,
                    infantNumericCombo
            )
            return FlightJourneyModel(
                    term,
                    id,
                    hasFreeRapidTest,
                    isSeatDistancing,
                    departureAirport,
                    departureAirportName,
                    departureAirportCity,
                    departureTime,
                    departureTimeInt,
                    arrivalAirport,
                    arrivalTime,
                    arrivalAirportName,
                    arrivalAirportCity,
                    arrivalTimeInt,
                    totalTransit,
                    addDayArrival,
                    duration,
                    durationMinute,
                    total,
                    totalNumeric,
                    totalCombo,
                    0,
                    isBestPairing,
                    beforeTotal,
                    false,
                    isRefundable,
                    isReturn,
                    fare,
                    routes,
                    flightAirlineDBS ?: arrayListOf(),
                    comboId,
                    ""
            )
        }
    }

}