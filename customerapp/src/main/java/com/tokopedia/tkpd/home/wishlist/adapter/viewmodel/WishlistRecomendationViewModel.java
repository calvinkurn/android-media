package com.tokopedia.tkpd.home.wishlist.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistTypeFactory;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistRecomendationViewModel implements Visitable<WishlistTypeFactory> {

    private RecommendationItem recommendationItem;

    public WishlistRecomendationViewModel(RecommendationItem recommendationItem) {
        this.recommendationItem = recommendationItem;
    }

    public RecommendationItem getRecommendationItem() {
        return recommendationItem;
    }

    @Override
    public int type(WishlistTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
