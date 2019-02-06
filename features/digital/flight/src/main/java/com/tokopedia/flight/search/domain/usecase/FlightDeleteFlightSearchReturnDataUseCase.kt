package com.tokopedia.flight.search.domain.usecase

import com.tokopedia.flight.search.data.repository.FlightSearchRepository
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