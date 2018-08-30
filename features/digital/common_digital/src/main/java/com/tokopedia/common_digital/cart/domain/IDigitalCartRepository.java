package com.tokopedia.common_digital.cart.domain;

import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;

import rx.Observable;

/**
 * Created by Rizky on 27/08/18.
 */
public interface IDigitalCartRepository {

    Observable<CartDigitalInfoData> addToCart(
            RequestBodyAtcDigital requestBodyAtcDigital, String idemPotencyKeyHeader
    );

    Observable<InstantCheckoutData> instantCheckout(RequestBodyCheckout requestBodyCheckout);

}