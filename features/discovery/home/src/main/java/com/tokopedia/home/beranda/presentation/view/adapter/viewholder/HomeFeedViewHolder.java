package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;
import com.tokopedia.productcard.ProductCardView;

public class HomeFeedViewHolder extends AbstractViewHolder<HomeFeedViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.home_feed_item;

    private ProductCardView productCardView;

    public HomeFeedViewHolder(View itemView) {
        super(itemView);
        productCardView = itemView.findViewById(R.id.productCardView);
    }

    @Override
    public void bind(HomeFeedViewModel element) {
        productCardView.setImageUrl(element.getImageUrl());
        productCardView.setTitle(element.getProductName());
        productCardView.setPrice(element.getPrice());
        productCardView.setTopAdsVisible(element.isTopAds());
    }
}
