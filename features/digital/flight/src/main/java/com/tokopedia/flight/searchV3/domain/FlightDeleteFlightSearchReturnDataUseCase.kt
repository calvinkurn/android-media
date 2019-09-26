package com.tokopedia.flight.searchV3.domain

import com.tokopedia.flight.searchV3.data.repository.FlightSearchRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 12/10/18.
 */
class FlightDeleteFlightSearchReturnDataUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams?): Observable<Boolean> {
        return flightSearchRepository.deleteFlightSearchReturnData()
                .map { true }
    }
}