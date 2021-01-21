package com.tokopedia.flight.searchV4.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.searchV4.data.FlightSearchRepository
import com.tokopedia.flight.searchV4.data.cache.db.JourneyAndRoutes
import com.tokopedia.flight.searchV4.data.cloud.single.Amenity
import com.tokopedia.flight.searchV4.data.cloud.single.Info
import com.tokopedia.flight.searchV4.data.cloud.single.Route
import com.tokopedia.flight.searchV4.data.cloud.single.StopDetailEntity
import com.tokopedia.flight.searchV4.presentation.model.FlightFareModel
import com.tokopedia.flight.searchV4.presentation.model.FlightJourneyModel
import com.tokopedia.flight.searchV4.presentation.model.filter.FlightFilterModel
import javax.inject.Inject

/**
 * @author by furqan on 13/04/2020
 */
class FlightSortAndFilterUseCase @Inject constructor(private val flightSearchRepository: FlightSearchRepository) {

    suspend fun execute(@TravelSortOption flightSortOption: Int,
                        flightFilterModel: FlightFilterModel): List<FlightJourneyModel> =
            flightSearchRepository.getSearchFilter(flightSortOption, flightFilterModel).let {
                mapToFlightJourneyViewModel(it.journeyAndRoutes, it.specialTag)
            }

    private fun mapToFlightJourneyViewModel(it: List<JourneyAndRoutes>, specialTag: String): List<FlightJourneyModel> {
        val gson = Gson()
        return it.map { journeyAndRoutes ->
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
                FlightJourneyModel(
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
                        totalNumericCombo,
                        isBestPairing,
                        beforeTotal,
                        isShowSpecialPriceTag,
                        isRefundable,
                        isReturn,
                        fare,
                        routes,
                        flightAirlineDBS ?: arrayListOf(),
                        comboId,
                        specialTag
                )
            }
        }
    }

}