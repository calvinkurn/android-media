package com.tokopedia.flight.searchV2.domain

import com.tokopedia.flight.searchV2.data.repository.FlightSearchRepository
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchApiRequestModel
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchMetaViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * Created by Rizky on 26/09/18.
 */
class FlightSearchUseCase(val flightSearchRepository: FlightSearchRepository) : UseCase<FlightSearchMetaViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<FlightSearchMetaViewModel>? {
        return flightSearchRepository.getSearchSingle(requestParams.parameters)
    }

    fun createReqestParams(flightSearchApiRequestModel: FlightSearchApiRequestModel) : RequestParams {
        val requestParams = RequestParams.create()
        return requestParams
    }

}