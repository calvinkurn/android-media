package com.tokopedia.posapp.data.mapper;

import com.tokopedia.posapp.domain.model.cart.AddToCartStatusDomain;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import rx.functions.Func1;

/**
 * Created by okasurya on 8/22/17.
 */

public class AddToCartMapper implements Func1<CartDomain, AddToCartStatusDomain> {
    @Override
    public AddToCartStatusDomain call(CartDomain cartDomain) {
        AddToCartStatusDomain status = new AddToCartStatusDomain();
        status.setStatus(AddToCartStatusDomain.RESULT_ADD_TO_CART_SUCCESS);
        status.setMessage("Success");

        return status;
    }
}
