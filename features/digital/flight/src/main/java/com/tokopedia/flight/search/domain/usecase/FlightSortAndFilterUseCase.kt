package com.tokopedia.flight.search.domain.usecase

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.search.data.api.single.response.Amenity
import com.tokopedia.flight.search.data.api.single.response.Info
import com.tokopedia.flight.search.data.api.single.response.Route
import com.tokopedia.flight.search.data.api.single.response.StopDetailEntity
import com.tokopedia.flight.search.data.db.JourneyAndRoutes
import com.tokopedia.flight.search.data.repository.FlightSearchRepository
import com.tokopedia.flight.search.presentation.model.FlightFareViewModel
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 26/09/18.
 */
class FlightSortAndFilterUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) : UseCase<List<@JvmSuppressWildcards FlightJourneyViewModel>>() {

    private val PARAM_SORT = "PARAM_SORT"
    private val PARAM_FILTER_MODEL = "PARAM_FILTER_MODEL"
    private val PARAM_IS_USE_BEST_PAIR = "PARAM_IS_USE_BEST_PAIR"

    override fun createObservable(requestParams: RequestParams): Observable<List<@JvmSuppressWildcards FlightJourneyViewModel>> {
        val sortOption = requestParams.getInt(PARAM_SORT, TravelSortOption.CHEAPEST)
        val filterModel = requestParams.getObject(PARAM_FILTER_MODEL) as FlightFilterModel
        val isUseBestPair = requestParams.getBoolean(PARAM_IS_USE_BEST_PAIR, true)

        return flightSearchRepository.getSearchFilter(sortOption, filterModel)
                    .map { mapToFlightJourneyViewModel(it, isUseBestPair) }
    }

    private fun mapToFlightJourneyViewModel(it: List<JourneyAndRoutes>, isUseBestPair: Boolean): List<FlightJourneyViewModel> {
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
                        it.arrivalAirportCity
                )
            }
            with(journeyAndRoutes.flightJourneyTable) {
                if (isUseBestPair) {
                    val fare = FlightFareViewModel(
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
                    FlightJourneyViewModel(
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
                            totalNumericCombo,
                            isBestPairing,
                            beforeTotal,
                            isRefundable,
                            isReturn,
                            fare,
                            routes,
                            flightAirlineDBS,
                            comboId
                    )
                } else {
                    val fare = FlightFareViewModel(
                            adult,
                            "0",
                            child,
                            "0",
                            infant,
                            "0",
                            adultNumeric,
                            0,
                            childNumeric,
                            0,
                            infantNumeric,
                            0
                    )
                    FlightJourneyViewModel(
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
                            totalNumericCombo,
                            false,
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
        }
    }

    fun createRequestParams(@TravelSortOption flightSortOption: Int,
                            flightFilterModel: FlightFilterModel,
                            isUseBestPair: Boolean): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_SORT, flightSortOption)
        requestParams.putObject(PARAM_FILTER_MODEL, flightFilterModel)
        requestParams.putBoolean(PARAM_IS_USE_BEST_PAIR, isUseBestPair)
        return requestParams
    }

}