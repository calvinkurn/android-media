package com.tokopedia.flight.searchV2.domain

import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel
import com.tokopedia.flight.searchV2.data.repository.FlightSearchRepository
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
    private val PARAM_IS_RETURN = "is_return"
    private val PARAM_IS_ROUND_TRIP = "is_round_trip"
    private val PARAM_ONWARD_JOURNEY_ID = "onward_journey_id"

    override fun createObservable(requestParams: RequestParams): Observable<FlightSearchMetaViewModel> {
        val isRoundTrip = requestParams.getBoolean(PARAM_IS_ROUND_TRIP, false)
        val isReturnTrip = requestParams.getBoolean(PARAM_IS_RETURN, false)
        val onwardJourneyId = requestParams.getString(PARAM_ONWARD_JOURNEY_ID, "")

        val flightSearchApiRequestModel =
                requestParams.getObject(PARAM_INITIAL_SEARCH) as FlightSearchApiRequestModel

        if (isRoundTrip && !isReturnTrip) {
            return flightSearchRepository.getSearchSingleCombined(requestParams.parameters, isReturnTrip)
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
                                    airlines
                            )
                        }
                    }
        } else if (isRoundTrip && isReturnTrip) {
            return flightSearchRepository.getSearchCombinedReturn(requestParams.parameters, onwardJourneyId,
                    isReturnTrip)
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
                                    airlines
                            )
                        }
                    }
        } else {
            return flightSearchRepository.getSearchSingle(requestParams.parameters, isReturnTrip)
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
                                    airlines
                            )
                        }
                    }
        }
    }

    fun createRequestParams(flightSearchSingleApiRequestModel: FlightSearchApiRequestModel,
                            isReturnTrip: Boolean,
                            isRoundTrip: Boolean,
                            onwardJourneyId: String?) : RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_INITIAL_SEARCH, flightSearchSingleApiRequestModel)
        requestParams.putBoolean(PARAM_IS_RETURN, isReturnTrip)
        requestParams.putBoolean(PARAM_IS_ROUND_TRIP, isRoundTrip)
        requestParams.putString(PARAM_ONWARD_JOURNEY_ID, onwardJourneyId)
        return requestParams
    }

}