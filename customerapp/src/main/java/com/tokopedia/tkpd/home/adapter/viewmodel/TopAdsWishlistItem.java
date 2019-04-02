package com.tokopedia.tkpd.home.adapter.viewmodel;

import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

/**
 * Author errysuprayogi on 25,November,2018
 */
public class TopAdsWishlistItem extends RecyclerViewItem {

    private TopAdsModel topAdsModel;
    private String query = "";

    public TopAdsWishlistItem() {
        setType(TkpdState.RecyclerView.VIEW_TOP_ADS_LIST);
    }

    public TopAdsWishlistItem(TopAdsModel topAdsModel, String query) {
        this.topAdsModel = topAdsModel;
        this.query = query;
        setType(TkpdState.RecyclerView.VIEW_TOP_ADS_LIST);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public TopAdsModel getTopAdsModel() {
        return topAdsModel;
    }

    public void setTopAdsModel(TopAdsModel topAdsModel) {
        this.topAdsModel = topAdsModel;
    }

}
