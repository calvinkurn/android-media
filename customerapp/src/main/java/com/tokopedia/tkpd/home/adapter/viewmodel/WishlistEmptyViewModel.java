package com.tokopedia.tkpd.home.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpd.home.adapter.factory.WishlistTypeFactory;

/**
 * Author errysuprayogi on 03,July,2019
 */
public class WishlistEmptyViewModel implements Visitable<WishlistTypeFactory> {

    @Override
    public int type(WishlistTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
