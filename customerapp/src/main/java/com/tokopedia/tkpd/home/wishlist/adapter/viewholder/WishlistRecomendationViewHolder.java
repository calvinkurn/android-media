package com.tokopedia.tkpd.home.wishlist.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecomendationViewModel;
import com.tokopedia.tkpd.home.wishlist.analytics.WishlistAnalytics;

import org.jetbrains.annotations.NotNull;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistRecomendationViewHolder extends AbstractViewHolder<WishlistRecomendationViewModel> implements RecommendationCardView.TrackingListener {

    @LayoutRes
    public static int LAYOUT = R.layout.wishlist_item_recomnedation;
    private RecommendationCardView recommendationCardView;
    private WishlistAnalytics wishlistAnalytics;

    public WishlistRecomendationViewHolder(View itemView, WishlistAnalytics wishlistAnalytics) {
        super(itemView);
        this.wishlistAnalytics = wishlistAnalytics;
        recommendationCardView = itemView.findViewById(com.tokopedia.navigation.R.id.productCardView);
    }

    @Override
    public void bind(WishlistRecomendationViewModel element) {
        recommendationCardView.setRecommendationModel(element.getRecommendationItem(), this);
        RecommendationItem item = element.getRecommendationItem();
        if (item.isTopAds()) {
            recommendationCardView.setRecommendationModel(item, this);
        }
    }

    @Override
    public void onImpressionTopAds(@NotNull RecommendationItem item) {
        wishlistAnalytics.eventEmptyWishlistProductImpressions(item, item.getPosition());
    }

    @Override
    public void onImpressionOrganic(@NotNull RecommendationItem item) {
        wishlistAnalytics.eventEmptyWishlistProductImpressions(item, item.getPosition());
    }

    @Override
    public void onClickTopAds(@NotNull RecommendationItem item) {
        wishlistAnalytics.eventEmptyWishlistProductClick(item, item.getPosition());
    }

    @Override
    public void onClickOrganic(@NotNull RecommendationItem item) {
        wishlistAnalytics.eventEmptyWishlistProductClick(item, item.getPosition());
    }
}
