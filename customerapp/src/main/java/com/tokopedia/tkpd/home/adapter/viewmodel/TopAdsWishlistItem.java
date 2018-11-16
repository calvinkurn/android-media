package com.tokopedia.tkpd.home.adapter.viewmodel;

import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

/**
 * Author errysuprayogi on 25,November,2018
 */
public class TopAdsWishlistItem extends RecyclerViewItem {

    private TopAdsModel topAdsModel;

    public TopAdsWishlistItem() {
        setType(TkpdState.RecyclerView.VIEW_TOP_ADS_LIST);
    }

    public TopAdsWishlistItem(TopAdsModel topAdsModel) {
        this.topAdsModel = topAdsModel;
        setType(TkpdState.RecyclerView.VIEW_TOP_ADS_LIST);
    }

    public TopAdsModel getTopAdsModel() {
        return topAdsModel;
    }

    public void setTopAdsModel(TopAdsModel topAdsModel) {
        this.topAdsModel = topAdsModel;
    }

}
