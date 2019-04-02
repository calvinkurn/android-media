package com.tokopedia.posapp.cart.view.subscriber;

import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.posapp.cart.view.CartMenu;

import java.util.List;

import rx.Subscriber;

/**
 * @author okasurya on 3/27/18.
 */

public class CartMenuSubscriber extends Subscriber<List<CartDomain>> {
    private CartMenu.View view;

    public CartMenuSubscriber(CartMenu.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.onCartEmpty();
    }

    @Override
    public void onNext(List<CartDomain> cartDomains) {
        if(cartDomains != null && !cartDomains.isEmpty()) {
            view.onCartFilled(cartDomains.size());
        } else {
            view.onCartEmpty();
        }
    }
}
