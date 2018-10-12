package com.tokopedia.flight.searchV2.domain

import com.tokopedia.flight.searchV2.data.repository.FlightSearchRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 12/10/18.
 */
class FlightDeleteSearchUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams?): Observable<Boolean> {
//        flightSearchRepository.deleteAllFlightSearchData()

        return Observable.just(true)
    }

    fun createRequestParams() : RequestParams {
        val requestParams = RequestParams.create()
        return requestParams
    }

}