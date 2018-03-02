package com.tokopedia.posapp.cart.view;

import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;

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
