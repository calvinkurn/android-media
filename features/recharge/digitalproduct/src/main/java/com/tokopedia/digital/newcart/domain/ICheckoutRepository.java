package com.tokopedia.digital.newcart.domain;

import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData;

import rx.Observable;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public interface ICheckoutRepository {

    Observable<CheckoutDigitalData> checkoutCart(RequestBodyCheckout requestBodyCheckout);

}
