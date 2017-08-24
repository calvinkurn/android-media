package com.tokopedia.posapp.data.repository;

import com.tokopedia.posapp.data.factory.CartFactory;
import com.tokopedia.posapp.domain.model.cart.AddToCartStatusDomain;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/22/17.
 */

public class CartRepositoryImpl implements CartRepository {
    CartFactory cartFactory;

    public CartRepositoryImpl(CartFactory cartFactory) {
        this.cartFactory = cartFactory;
    }

    @Override
    public Observable<AddToCartStatusDomain> addToCart(CartDomain cartDomain) {
        return cartFactory.getCartFromLocal().addToCart(cartDomain);
    }
}
