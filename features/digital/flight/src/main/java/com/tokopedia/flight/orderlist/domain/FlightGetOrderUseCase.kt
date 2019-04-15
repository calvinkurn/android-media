package com.tokopedia.flight.orderlist.domain

import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.orderlist.domain.model.FlightOrder
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class FlightGetOrderUseCase(private val flightRepository: FlightRepository) : UseCase<FlightOrder>() {

    override fun createObservable(requestParams: RequestParams): Observable<FlightOrder> {
        return flightRepository.getOrder(requestParams.getString(PARAM_ID, DEFAULT_EMPTY_VALUE))
    }

    fun createRequestParams(id: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_ID, id)
        return requestParams
    }

    companion object {
        private val PARAM_ID = "invoice_id"
        private val DEFAULT_EMPTY_VALUE = ""
    }
}
