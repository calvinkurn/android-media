package com.tokopedia.posapp.cart.data.factory;

import com.tokopedia.posapp.cart.CartLocalSource;

/**
 * Created by okasurya on 8/22/17.
 * Deprecated because of unnecessary layer
 */
@Deprecated
public class CartFactory {
    public CartFactory() {

    }

    public CartLocalSource local() {
        return new CartLocalSource();
    }
}
