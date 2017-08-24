package com.tokopedia.posapp.view.subscriber;

import com.tokopedia.posapp.domain.model.cart.AddToCartStatusDomain;
import com.tokopedia.posapp.view.AddToCart;
import com.tokopedia.posapp.view.Product;

import rx.Subscriber;

/**
 * Created by okasurya on 8/22/17.
 */

public class AddToCartSubscriber extends Subscriber<AddToCartStatusDomain> {
    AddToCart.View view;

    public AddToCartSubscriber(AddToCart.View view) {
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
    public void onNext(AddToCartStatusDomain status) {
        switch (status.getStatus()) {
            case AddToCartStatusDomain.RESULT_ADD_TO_CART_SUCCESS:
                view.onSuccessAddToCart(status.getMessage());
                break;
            default:
                view.onErrorAddToCart(status.getMessage());
                break;
        }
    }
}
