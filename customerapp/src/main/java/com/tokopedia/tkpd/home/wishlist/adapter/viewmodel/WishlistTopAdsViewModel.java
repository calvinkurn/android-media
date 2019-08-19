package com.tokopedia.tkpd.home.wishlist.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistTypeFactory;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistTopAdsViewModel implements Visitable<WishlistTypeFactory> {

    private String query = "";
    private String title = "";
    private TopAdsModel topAdsModel;

    public WishlistTopAdsViewModel(TopAdsModel topAdsModel, String query, String title) {
        this.query = query;
        this.topAdsModel = topAdsModel;
        this.title = title;
    }

    public String getQuery() {
        return query;
    }

    public String getTitle() {
        return title;
    }

    public TopAdsModel getTopAdsModel() {
        return topAdsModel;
    }

    @Override
    public int type(WishlistTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
