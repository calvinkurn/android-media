package com.tokopedia.tkpd.home.wishlist.adapter.viewholder;

import android.view.View;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecommendationCarouselItemViewModel;

/**
 * Created by Lukas on 2019-08-14
 */
public class WishlistRecommendationCarouselItemViewHolder extends AbstractViewHolder<WishlistRecommendationCarouselItemViewModel>{
    public static int LAYOUT = R.layout.layout_wishlist_carousel_item;

    public WishlistRecommendationCarouselItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(WishlistRecommendationCarouselItemViewModel element) {
        RecommendationCardView recommendationCardView = itemView.findViewById(R.id.recommendation_card);
        recommendationCardView.setRecommendationModel(element.getProductItem(), element.getListener());
        recommendationCardView.setWishlistButtonVisible(true);
        recommendationCardView.setButtonWishlistImage(element.getProductItem().isWishlist());
        recommendationCardView.setWishlistListener(element.getProductItem(), element.getWishlistListener());
        recommendationCardView.setFixedHeight(true);
    }
}
