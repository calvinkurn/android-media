package com.tokopedia.flight.search.domain.usecase

import com.tokopedia.flight.search.data.repository.FlightSearchRepository
import com.tokopedia.flight.search.presentation.model.FlightSearchCombinedApiRequestModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Rizky on 10/10/18.
 */
class FlightSearchCombinedUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository): UseCase<Boolean>() {

    private val PARAM_SEARCH_COMBINED = "search_combined"

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val flightSearchCombinedApiRequestModel =
                requestParams.getObject(PARAM_SEARCH_COMBINED) as FlightSearchCombinedApiRequestModel

        val numOfAttempts = intArrayOf(0)
        val pollDelay = intArrayOf(0)

        return flightSearchRepository.getSearchCombined(flightSearchCombinedApiRequestModel).doOnNext {
            pollDelay[0] = it.refreshTime
            numOfAttempts[0]++
        }.repeatWhen { o ->
            o.delay(pollDelay[0].toLong(), TimeUnit.SECONDS)
            o.flatMap { Observable.timer(pollDelay[0].toLong(), TimeUnit.SECONDS) }
        }.takeUntil {
            (!it.isNeedRefresh) && (numOfAttempts[0] >= it.maxRetry)
        }.last().map { true }
    }

    fun createRequestParam(flightSearchCombinedApiRequestModel: FlightSearchCombinedApiRequestModel)
            : RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_SEARCH_COMBINED, flightSearchCombinedApiRequestModel)
        return requestParams
    }

}