package com.tokopedia.flight.searchV2.domain.usecase

import com.tokopedia.flight.searchV2.data.repository.FlightSearchRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 12/10/18.
 */
class FlightDeleteAllFlightSearchDataUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams?): Observable<Boolean> {
        return flightSearchRepository.deleteAllFlightSearchData()
                .map { true }
    }
}