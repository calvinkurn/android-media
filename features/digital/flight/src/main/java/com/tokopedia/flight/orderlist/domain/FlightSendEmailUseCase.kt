package com.tokopedia.flight.orderlist.domain

import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import rx.Observable

/**
 * @author by alvarisi on 12/6/17.
 */

class FlightSendEmailUseCase(private val flightRepository: FlightRepository) : UseCase<SendEmailEntity>() {

    override fun createObservable(requestParams: RequestParams): Observable<SendEmailEntity> {
        return flightRepository.sendEmail(requestParams.parameters)
    }

    fun createRequestParams(orderId: String, userId: String, email: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_ID, orderId)
        requestParams.putString(USER_ID, userId)
        requestParams.putString(PARAM_EMAIL, email)
        return requestParams
    }

    companion object {
        private val PARAM_ID = "invoice_id"
        private val USER_ID = "uid"
        private val PARAM_EMAIL = "email"
    }
}
