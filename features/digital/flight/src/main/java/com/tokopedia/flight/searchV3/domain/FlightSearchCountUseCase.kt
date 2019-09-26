package com.tokopedia.flight.searchV3.domain

import com.tokopedia.flight.searchV3.data.repository.FlightSearchRepository
import com.tokopedia.flight.searchV3.presentation.model.filter.FlightFilterModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 15/10/18.
 */
class FlightSearchCountUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) : UseCase<Int>() {

    private val PARAM_FILTER_MODEL = "PARAM_FILTER_MODEL"

    override fun createObservable(requestParams: RequestParams): Observable<Int> {
        val filterModel = requestParams.getObject(PARAM_FILTER_MODEL) as FlightFilterModel

        return flightSearchRepository.getSearchCount(filterModel)
    }

    fun createRequestParams(flightFilterModel: FlightFilterModel): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_FILTER_MODEL, flightFilterModel)
        return requestParams
    }

}