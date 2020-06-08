package com.tokopedia.common_digital.cart.domain

import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData
import com.tokopedia.usecase.RequestParams

import rx.Observable

/**
 * Created by Rizky on 27/08/18.
 */
interface IDigitalCartRepository {

    fun addToCart(
            requestBodyAtcDigital: RequestBodyAtcDigital, idemPotencyKeyHeader: String
    ): Observable<CartDigitalInfoData>

    fun getCart(requestParam: RequestParams): Observable<CartDigitalInfoData>

    fun instantCheckout(requestBodyCheckout: RequestBodyCheckout): Observable<InstantCheckoutData>

}