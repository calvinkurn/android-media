package com.tokopedia.flight.search.domain.usecase

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.flight.search.data.api.single.response.Amenity
import com.tokopedia.flight.search.data.api.single.response.Info
import com.tokopedia.flight.search.data.api.single.response.Route
import com.tokopedia.flight.search.data.api.single.response.StopDetailEntity
import com.tokopedia.flight.search.data.db.JourneyAndRoutes
import com.tokopedia.flight.search.data.repository.FlightSearchRepository
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 15/10/18.
 */
class FlightSearchJourneyByIdUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) : UseCase<FlightJourneyViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<FlightJourneyViewModel> {
        val journeyId = requestParams.getString("PARAM_JOURNEY_ID", "")

        return flightSearchRepository.getSearchJourneyById(journeyId)
                .map { mapToFlightJourneyViewModel(it) }
    }

    private fun mapToFlightJourneyViewModel(journeyAndRoutes: JourneyAndRoutes): FlightJourneyViewModel {
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
                    it.arrivalAirportCity
            )
        }
        with(journeyAndRoutes.flightJourneyTable) {
            val fare = com.tokopedia.flight.search.presentation.model.FlightFareViewModel(
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
            return FlightJourneyViewModel(
                    term,
                    id,
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
                    isRefundable,
                    isReturn,
                    fare,
                    routes,
                    flightAirlineDBS,
                    comboId
            )
        }
    }

    fun createRequestParams(journeyId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString("PARAM_JOURNEY_ID", journeyId)
        return requestParams
    }

}