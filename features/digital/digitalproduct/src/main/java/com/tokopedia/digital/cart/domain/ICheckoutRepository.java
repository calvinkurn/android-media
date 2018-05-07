package com.tokopedia.digital.cart.domain;

import com.tokopedia.digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.cart.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.model.InstantCheckoutData;

import rx.Observable;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public interface ICheckoutRepository {

    Observable<CheckoutDigitalData> checkoutCart(RequestBodyCheckout requestBodyCheckout);

    Observable<InstantCheckoutData> instantCheckoutCart(RequestBodyCheckout requestBodyCheckout);
}
