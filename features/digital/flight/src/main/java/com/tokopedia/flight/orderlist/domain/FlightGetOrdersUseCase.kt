package com.tokopedia.flight.orderlist.domain

import android.text.TextUtils

import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.orderlist.domain.model.FlightOrder
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import rx.Observable

class FlightGetOrdersUseCase(private val flightRepository: FlightRepository) : UseCase<List<FlightOrder>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<FlightOrder>> {
        return flightRepository.getOrders(requestParams.parameters)
    }

    fun createRequestParam(page: Int, status: String, perPage: Int): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt(PARAM_PAGE, page)
        if (!TextUtils.isEmpty(status)) {
            requestParams.putString(PARAM_STATUS, status)
        }
        requestParams.putInt(PARAM_PER_PAGE, if (perPage == 0) DEFAULT_PER_PAGE_VALUE else perPage)
        return requestParams
    }

    companion object {
        private val PARAM_STATUS = "status_bulk"
        private val PARAM_PAGE = "page"
        private val PARAM_PER_PAGE = "per_page"
        private val DEFAULT_PER_PAGE_VALUE = 10
    }
}
