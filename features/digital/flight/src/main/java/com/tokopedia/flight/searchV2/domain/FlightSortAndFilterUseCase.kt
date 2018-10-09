package com.tokopedia.flight.searchV2.domain

import android.text.TextUtils
import com.tokopedia.flight.search.constant.FlightSortOption
import com.tokopedia.flight.search.data.cloud.model.response.Route
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
        private val flightSearchRepository: FlightSearchRepository) : UseCase<List<FlightJourneyViewModel>>() {

    private val PARAM_SORT = "PARAM_SORT"
    private val PARAM_FILTER_MODEL = "PARAM_FILTER_MODEL"

    override fun createObservable(requestParams: RequestParams): Observable<List<FlightJourneyViewModel>> {
        val sortOption = requestParams.getObject(PARAM_FILTER_MODEL) as FlightSortOption
        val filterModel = requestParams.getObject(PARAM_FILTER_MODEL) as FlightFilterModel

        return if (!TextUtils.isEmpty(filterModel.journeyId)) {
            flightSearchRepository.getSearchReturnBestPairsByOnwardJourneyId(filterModel.journeyId)
                    .map { it ->
                        it.map { journeyAndRoutes ->
                            val routes = journeyAndRoutes.routes.map {
                                Route(
                                        it.airline,
                                        it.departureAirport,
                                        it.departureTimestamp,
                                        it.arrivalAirport,
                                        it.arrivalTimestamp,
                                        it.duration,
                                        it.layover,
                                        null,
                                        it.flightNumber,
                                        it.isRefundable,
                                        null,
                                        it.stops,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
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
        } else {
            flightSearchRepository.getSearchFilter(sortOption, filterModel)
                    .map { it ->
                        it.map { journerAndRoutes ->
                            val routes = journerAndRoutes.routes.map {
                                Route(
                                        it.airline,
                                        it.departureAirport,
                                        it.departureTimestamp,
                                        it.arrivalAirport,
                                        it.arrivalTimestamp,
                                        it.duration,
                                        it.layover,
                                        null,
                                        it.flightNumber,
                                        it.isRefundable,
                                        null,
                                        it.stops,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
                                )
                            }
                            with(journerAndRoutes.flightJourneyTable) {
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