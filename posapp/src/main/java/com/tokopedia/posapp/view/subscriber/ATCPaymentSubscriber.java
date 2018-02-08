package com.tokopedia.posapp.view.subscriber;

import com.tokopedia.posapp.domain.model.cart.ATCStatusDomain;
import com.tokopedia.posapp.view.AddToCart;

import rx.Subscriber;

/**
 * Created by okasurya on 9/18/17.
 */

public class ATCPaymentSubscriber extends Subscriber<ATCStatusDomain> {
    AddToCart.View view;

    public ATCPaymentSubscriber(AddToCart.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.onErrorAddToCart(e.getMessage());
    }

    @Override
    public void onNext(ATCStatusDomain atcStatusDomain) {
        view.onSuccessATCPayment(atcStatusDomain.getMessage());
    }
}
