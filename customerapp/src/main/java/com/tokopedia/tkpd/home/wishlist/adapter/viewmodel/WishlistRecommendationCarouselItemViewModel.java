package com.tokopedia.tkpd.home.wishlist.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistTypeFactory;
import com.tokopedia.tkpd.home.wishlist.adapter.viewholder.WishlistRecommendationCarouselItemViewHolder;

/**
 * Created by Lukas on 2019-08-14
 */
public class WishlistRecommendationCarouselItemViewModel implements Visitable<WishlistTypeFactory> {
    private RecommendationItem productItem;
    private RecommendationCardView.TrackingListener listener;
    private RecommendationCardView.WishlistListener wishlistListener;

    public WishlistRecommendationCarouselItemViewModel(RecommendationItem productItem, RecommendationCardView.TrackingListener listener, RecommendationCardView.WishlistListener wishlistListener) {
        this.productItem = productItem;
        this.listener = listener;
        this.wishlistListener = wishlistListener;
    }

    public RecommendationItem getProductItem() {
        return productItem;
    }

    public RecommendationCardView.TrackingListener getListener() {
        return listener;
    }

    public RecommendationCardView.WishlistListener getWishlistListener() {
        return wishlistListener;
    }

    @Override
    public int type(WishlistTypeFactory typeFactory) {
        return WishlistRecommendationCarouselItemViewHolder.LAYOUT;
    }
}
