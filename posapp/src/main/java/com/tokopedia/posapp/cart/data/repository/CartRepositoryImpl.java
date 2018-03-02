package com.tokopedia.posapp.cart.data.repository;


import com.tokopedia.posapp.cart.data.factory.CartFactory;
import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;

import java.util.List;

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
    public Observable<ATCStatusDomain> storeCartProduct(CartDomain cartDomain) {
        return cartFactory.local().storeCartProduct(cartDomain);
    }

    @Override
    public Observable<ATCStatusDomain> updateCartProduct(CartDomain cartDomain) {
        return cartFactory.local().updateCartProduct(cartDomain);
    }

    @Override
    public Observable<ATCStatusDomain> deleteCartProduct(CartDomain cartDomain) {
        return cartFactory.local().deleteCartProduct(cartDomain);
    }

    @Override
    public Observable<ATCStatusDomain> deleteCart() {
        return cartFactory.local().deleteCart();
    }

    @Override
    public Observable<CartDomain> getCartProduct(int productId) {
        return cartFactory.local().getCartProduct(productId);
    }

    @Override
    public Observable<List<CartDomain>> getAllCartProducts() {
        return cartFactory.local().getAllCartProducts();
    }
}
