package com.tokopedia.posapp.cart.data.repository;

import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 8/22/17.
 */

public interface CartRepository {
    String CART = "CART";
    String EXISTING_CART = "EXISTING_CART";

    Observable<ATCStatusDomain> storeCartProduct(RequestParams cartDomain);

    Observable<ATCStatusDomain> updateCartProduct(RequestParams requestParams);

    Observable<ATCStatusDomain> deleteCartProduct(CartDomain cartDomain);

    Observable<ATCStatusDomain> deleteCart();

    Observable<CartDomain> getCartProduct(RequestParams requestParams);

    Observable<List<CartDomain>> getAllCartProducts();
}
