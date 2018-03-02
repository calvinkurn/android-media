package com.tokopedia.posapp.cart.view;

import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;

import rx.Subscriber;

/**
 * Created by okasurya on 8/22/17.
 */

public class ATCSubscriber extends Subscriber<ATCStatusDomain> {
    AddToCart.View view;

    public ATCSubscriber(AddToCart.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(ATCStatusDomain status) {
        switch (status.getStatus()) {
            case ATCStatusDomain.RESULT_ADD_TO_CART_SUCCESS:
                view.onSuccessAddToCart(status.getMessage());
                break;
            default:
                view.onErrorAddToCart(status.getMessage());
                break;
        }
    }
}
