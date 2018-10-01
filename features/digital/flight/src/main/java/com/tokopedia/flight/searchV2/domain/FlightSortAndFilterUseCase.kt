package com.tokopedia.flight.searchV2.domain

import com.tokopedia.flight.search.constant.FlightSortOption
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchViewModel
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * Created by Rizky on 26/09/18.
 */
class FlightSortAndFilterUseCase : UseCase<FlightSearchViewModel>() {

    override fun createObservable(requestParams: RequestParams?): Observable<FlightSearchViewModel> {
        return Observable.just(null)
    }

    fun createRequestParams(isReturning: Boolean,
                            flightSortOption: FlightSortOption,
                            flightFilterModel: FlightFilterModel): RequestParams {
        return RequestParams.create()
    }

}