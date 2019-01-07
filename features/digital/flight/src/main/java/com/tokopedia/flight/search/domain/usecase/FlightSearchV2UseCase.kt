package com.tokopedia.flight.search.domain.usecase

import com.tokopedia.flight.search.data.api.single.response.Meta
import com.tokopedia.flight.search.presentation.model.FlightSearchApiRequestModel
import com.tokopedia.flight.search.data.repository.FlightSearchRepository
import com.tokopedia.flight.search.presentation.model.FlightSearchMetaViewModel
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

        return if (isRoundTrip && !isReturnTrip) {
            flightSearchRepository.getSearchSingleCombined(requestParams.parameters, isReturnTrip)
                    .map { mapToFlightSearchMetaViewModel(it, flightSearchApiRequestModel) }
        } else if (isRoundTrip && isReturnTrip) {
            flightSearchRepository.getSearchCombinedReturn(requestParams.parameters, onwardJourneyId,
                    isReturnTrip)
                    .map { mapToFlightSearchMetaViewModel(it, flightSearchApiRequestModel) }
        } else {
            flightSearchRepository.getSearchSingle(requestParams.parameters, isReturnTrip)
                    .map { mapToFlightSearchMetaViewModel(it, flightSearchApiRequestModel) }
        }
    }

    private fun mapToFlightSearchMetaViewModel(meta: Meta,
                                               flightSearchApiRequestModel: FlightSearchApiRequestModel):
            FlightSearchMetaViewModel {
        with(meta) {
            return FlightSearchMetaViewModel(
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