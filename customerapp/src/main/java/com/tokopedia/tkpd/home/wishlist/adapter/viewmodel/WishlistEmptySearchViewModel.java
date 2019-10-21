package com.tokopedia.tkpd.home.wishlist.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistTypeFactory;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistEmptySearchViewModel implements Visitable<WishlistTypeFactory> {

    private String query = "";

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public int type(WishlistTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
