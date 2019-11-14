package com.tokopedia.tkpd.home.wishlist.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistTypeFactory;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistEmptyViewModel implements Visitable<WishlistTypeFactory> {

    @Override
    public int type(WishlistTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
