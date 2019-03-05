package com.tokopedia.flight.bookingV2.domain

import com.tokopedia.flight.bookingV2.data.entity.GetCartEntity
import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by furqan on 05/03/19
 */
class FlightGetCartDataUseCase @Inject constructor(val flightRepository: FlightRepository)
    : UseCase<GetCartEntity>() {

    override fun createObservable(requestParams: RequestParams?): Observable<GetCartEntity> =
            flightRepository.getCart(requestParams!!.getString(PARAM_CART_ID, ""))

    fun createRequestParam(cartId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_CART_ID, cartId)
        return requestParams
    }

    companion object {
        private val PARAM_CART_ID = "PARAM_CART_ID"
    }
}