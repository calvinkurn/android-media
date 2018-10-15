package com.tokopedia.flight.searchV2.domain

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.flight.search.constant.FlightSortOption
import com.tokopedia.flight.search.data.cloud.model.response.Amenity
import com.tokopedia.flight.search.data.cloud.model.response.Info
import com.tokopedia.flight.search.data.cloud.model.response.Route
import com.tokopedia.flight.search.data.cloud.model.response.StopDetailEntity
import com.tokopedia.flight.searchV2.data.db.JourneyAndRoutes
import com.tokopedia.flight.searchV2.data.repository.FlightSearchRepository
import com.tokopedia.flight.searchV2.presentation.model.FlightFareViewModel
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel
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

    override fun createObservable(requestParams: RequestParams): Observable<List<@JvmSuppressWildcards FlightJourneyViewModel>> {
        val sortOption = requestParams.getInt(PARAM_SORT, FlightSortOption.CHEAPEST)
        val filterModel = requestParams.getObject(PARAM_FILTER_MODEL) as FlightFilterModel

        return if (!TextUtils.isEmpty(filterModel.journeyId)) {
            flightSearchRepository.getSearchReturnBestPairsByOnwardJourneyId(filterModel)
                    .map { mapToFlightJourneyViewModel(it) }
        } else {
            flightSearchRepository.getSearchFilter(sortOption, filterModel)
                    .map { mapToFlightJourneyViewModel(it) }
        }
    }

    private fun mapToFlightJourneyViewModel(it: List<JourneyAndRoutes>): List<FlightJourneyViewModel> {
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
                        0,
                        isBestPairing,
                        beforeTotal,
                        isRefundable,
                        isReturn,
                        fare,
                        routes,
                        flightAirlineDBS
                )
            }
        }
    }

    fun createRequestParams(@FlightSortOption flightSortOption: Int,
                            flightFilterModel: FlightFilterModel): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_SORT, flightSortOption)
        requestParams.putObject(PARAM_FILTER_MODEL, flightFilterModel)
        return requestParams
    }

}