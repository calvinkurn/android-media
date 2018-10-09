package com.tokopedia.flight.searchV2.domain

import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel
import com.tokopedia.flight.searchV2.data.repository.FlightSearchRepository
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchCombinedApiRequestModel
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchMetaViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 26/09/18.
 */
class FlightSearchV2UseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) : UseCase<FlightSearchMetaViewModel>() {

    private val PARAM_INITIAL_SEARCH = "initial_search"
    private val PARAM_SEARCH_COMBINED = "search_combined"
    private val PARAM_IS_RETURNING = "is_return"
    private val PARAM_IS_ROUND_TRIP = "is_round_trip"
    private val PARAM_ONWARD_JOURNEY_ID = "onward_journey_id"

    override fun createObservable(requestParams: RequestParams): Observable<FlightSearchMetaViewModel> {
        val isRoundTrip = requestParams.getBoolean(PARAM_IS_ROUND_TRIP, false)
        val isReturning = requestParams.getBoolean(PARAM_IS_RETURNING, false)
        val onwardJourneyId = requestParams.getString(PARAM_ONWARD_JOURNEY_ID, "")

        val flightSearchApiRequestModel =
                requestParams.getObject(PARAM_INITIAL_SEARCH) as FlightSearchApiRequestModel

        if (isRoundTrip && !isReturning) {
            val flightSearchCombinedApiRequestModel =
                    requestParams.getObject(PARAM_SEARCH_COMBINED) as FlightSearchCombinedApiRequestModel
            return flightSearchRepository.getSearchCombined(flightSearchCombinedApiRequestModel)
                    .flatMap {
                        flightSearchRepository.getSearchSingleCombined(requestParams.parameters)
                                .map { meta ->
                                    with(meta) {
                                        return@map FlightSearchMetaViewModel(
                                                flightSearchApiRequestModel.depAirport,
                                                flightSearchApiRequestModel.arrAirport,
                                                flightSearchApiRequestModel.date,
                                                isNeedRefresh,
                                                refreshTime,
                                                maxRetry,
                                                0,
                                                0,
                                                arrayListOf()
                                        )
                                    }
                                }
                    }
        } else if (isRoundTrip && isReturning) {
            return flightSearchRepository.getSearchCombinedReturn(requestParams.parameters, onwardJourneyId)
                    .map { meta ->
                        with(meta) {
                            return@map FlightSearchMetaViewModel(
                                    flightSearchApiRequestModel.depAirport,
                                    flightSearchApiRequestModel.arrAirport,
                                    flightSearchApiRequestModel.date,
                                    isNeedRefresh,
                                    refreshTime,
                                    maxRetry,
                                    0,
                                    0,
                                    arrayListOf()
                            )
                        }
                    }
        } else {
            return flightSearchRepository.getSearchSingle(requestParams.parameters)
                    .map { meta ->
                        with(meta) {
                            return@map FlightSearchMetaViewModel(
                                    flightSearchApiRequestModel.depAirport,
                                    flightSearchApiRequestModel.arrAirport,
                                    flightSearchApiRequestModel.date,
                                    isNeedRefresh,
                                    refreshTime,
                                    maxRetry,
                                    0,
                                    0,
                                    arrayListOf()
                            )
                        }
                    }
        }
    }

    fun createRequestParams(flightSearchSingleApiRequestModel: FlightSearchApiRequestModel,
                            flightSearchCombinedApiRequestModel: FlightSearchCombinedApiRequestModel?,
                            isReturning: Boolean,
                            isRoundTrip: Boolean,
                            onwardJourneyId: String?) : RequestParams {
        val requestParams = RequestParams.create()
        if (flightSearchCombinedApiRequestModel != null) {
            requestParams.putObject(PARAM_SEARCH_COMBINED, flightSearchCombinedApiRequestModel)
        }
        if (flightSearchSingleApiRequestModel != null) {
            requestParams.putObject(PARAM_INITIAL_SEARCH, flightSearchSingleApiRequestModel)
        }
        requestParams.putBoolean(PARAM_IS_RETURNING, isReturning)
        requestParams.putBoolean(PARAM_IS_ROUND_TRIP, isRoundTrip)
        requestParams.putString(PARAM_ONWARD_JOURNEY_ID, onwardJourneyId)
        return requestParams
    }

}