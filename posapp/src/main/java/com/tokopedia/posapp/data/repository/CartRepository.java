package com.tokopedia.posapp.data.repository;

import com.tokopedia.posapp.domain.model.cart.ATCStatusDomain;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/22/17.
 */

public interface CartRepository {
    Observable<ATCStatusDomain> addToCart(CartDomain requestParams);
}
