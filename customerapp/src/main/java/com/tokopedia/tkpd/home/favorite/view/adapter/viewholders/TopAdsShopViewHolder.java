package com.tokopedia.tkpd.home.favorite.view.adapter.viewholders;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopViewModel;

/**
 * @author kulomady on 1/24/17.
 */

public class TopAdsShopViewHolder extends AbstractViewHolder<TopAdsShopViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.child_favorite_rec_shop;

    public TopAdsShopViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(TopAdsShopViewModel element) {

    }
}
