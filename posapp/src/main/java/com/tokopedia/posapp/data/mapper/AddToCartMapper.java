package com.tokopedia.posapp.data.mapper;

import com.tokopedia.posapp.domain.model.cart.ATCStatusDomain;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import rx.functions.Func1;

/**
 * Created by okasurya on 8/22/17.
 */

public class AddToCartMapper implements Func1<CartDomain, ATCStatusDomain> {
    @Override
    public ATCStatusDomain call(CartDomain cartDomain) {
        ATCStatusDomain status = new ATCStatusDomain();
        status.setStatus(ATCStatusDomain.RESULT_ADD_TO_CART_SUCCESS);
        status.setMessage("Success");

        return status;
    }
}
