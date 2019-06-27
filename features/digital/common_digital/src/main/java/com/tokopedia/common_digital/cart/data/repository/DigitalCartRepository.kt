package com.tokopedia.common_digital.cart.data.repository

import com.tokopedia.common_digital.cart.data.datasource.DigitalAddToCartDataSource
import com.tokopedia.common_digital.cart.data.datasource.DigitalInstantCheckoutDataSource
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout
import com.tokopedia.common_digital.cart.domain.IDigitalCartRepository
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData

import rx.Observable

/**
 * Created by Rizky on 27/08/18.
 */
class DigitalCartRepository(private val digitalAddToCartDataSource: DigitalAddToCartDataSource,
                            private val digitalInstantCheckoutDataSource: DigitalInstantCheckoutDataSource) : IDigitalCartRepository {

    override fun addToCart(requestBodyAtcDigital: RequestBodyAtcDigital, idemPotencyKeyHeader: String): Observable<CartDigitalInfoData> {
        return digitalAddToCartDataSource.addToCart(requestBodyAtcDigital, idemPotencyKeyHeader)
    }

    override fun instantCheckout(requestBodyCheckout: RequestBodyCheckout): Observable<InstantCheckoutData> {
        return digitalInstantCheckoutDataSource.instantCheckout(requestBodyCheckout)
    }

}
