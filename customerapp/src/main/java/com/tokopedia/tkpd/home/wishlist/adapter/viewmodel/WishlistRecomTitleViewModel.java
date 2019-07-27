package com.tokopedia.tkpd.home.wishlist.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistTypeFactory;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistRecomTitleViewModel implements Visitable<WishlistTypeFactory> {

    private String title = "";

    public WishlistRecomTitleViewModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int type(WishlistTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
