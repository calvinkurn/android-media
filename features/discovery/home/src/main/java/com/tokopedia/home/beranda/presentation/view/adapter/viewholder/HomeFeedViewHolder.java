package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;
import com.tokopedia.productcard.ProductCardView;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

public class HomeFeedViewHolder extends AbstractViewHolder<HomeFeedViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.home_feed_item;

    private ProductCardView productCardView;
    private final HomeFeedContract.View homeFeedview;

    public HomeFeedViewHolder(View itemView, HomeFeedContract.View homeFeedview) {
        super(itemView);
        this.homeFeedview = homeFeedview;
        productCardView = itemView.findViewById(R.id.productCardView);
    }

    @Override
    public void bind(HomeFeedViewModel element) {
        productCardView.setImageUrl(element.getImageUrl());
        productCardView.setTitle(element.getProductName());
        productCardView.setPrice(element.getPrice());
        productCardView.setTopAdsVisible(element.isTopAds());
        productCardView.setWishlistButtonVisible(false);
        productCardView.setSlashedPrice(element.getSlashedPrice());
        productCardView.setDiscount(element.getDiscountPercentage());
        productCardView.getImageView().setViewHintListener(element, new ImpressedImageView.ViewHintListener() {
            @Override
            public void onViewHint() {
                homeFeedview.onProductImpression(element, getAdapterPosition());
            }
        });
    }
}
