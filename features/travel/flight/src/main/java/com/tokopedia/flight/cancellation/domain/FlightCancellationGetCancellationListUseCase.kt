package com.tokopedia.flight.cancellation.domain

import com.tokopedia.flight.cancellation.domain.mapper.FlightOrderEntityToCancellationListMapper
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel
import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.orderlist.data.cache.FlightOrderDataCacheSource
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by furqan on 30/04/18.
 */

class FlightCancellationGetCancellationListUseCase @Inject
constructor(private val flightOrderDataCacheSource: FlightOrderDataCacheSource,
            private val flightOrderEntityToCancellationListMapper: FlightOrderEntityToCancellationListMapper,
            private val flightRepository: FlightRepository) : UseCase<List<FlightCancellationListViewModel>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<FlightCancellationListViewModel>> {
        return flightOrderDataCacheSource.isExpired
                .flatMap { aBoolean ->
                    if (aBoolean!!) {
                        flightRepository.getOrderEntity(requestParams.getString(INVOICE_ID_PARAM, DEFAULT_INVOICE_ID))
                    } else {
                        flightOrderDataCacheSource.cache
                    }
                }
                .flatMap { orderEntity -> Observable.just(flightOrderEntityToCancellationListMapper.transform(orderEntity)) }
    }

    fun createRequestParams(invoiceId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(INVOICE_ID_PARAM, invoiceId)
        return requestParams
    }

    companion object {

        private val INVOICE_ID_PARAM = "INVOICE_ID_PARAM"
        private val DEFAULT_INVOICE_ID = ""
    }

}
