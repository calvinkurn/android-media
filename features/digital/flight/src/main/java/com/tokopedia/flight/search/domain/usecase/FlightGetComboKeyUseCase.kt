package com.tokopedia.flight.search.domain.usecase

import com.tokopedia.flight.search.data.repository.FlightSearchRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by furqan on 11/03/19
 */
class FlightGetComboKeyUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository): UseCase<String>() {

    override fun createObservable(requestParams: RequestParams?): Observable<String> =
            flightSearchRepository.getComboKey(requestParams!!.getString(PARAM_ONWARD_JOURNEY, ""),
                    requestParams.getString(PARAM_RETURN_JOURNEY, ""))

    fun createRequestParams(onwardJourneyId: String, returnJourneyId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_ONWARD_JOURNEY, onwardJourneyId)
        requestParams.putString(PARAM_RETURN_JOURNEY, returnJourneyId)
        return requestParams
    }

    companion object {
        val PARAM_ONWARD_JOURNEY = "PARAM_ONWARD_JOURNEY"
        val PARAM_RETURN_JOURNEY = "PARAM_RETURN_JOURNEY"
    }
}