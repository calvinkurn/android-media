package com.tokopedia.tkpd.home.wishlist.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistTypeFactory;
import java.util.List;

/**
 * Created by Lukas on 2019-08-14
 */
public class WishlistRecommendationCarouselViewModel implements Visitable<WishlistTypeFactory> {
    private List<RecommendationItem> recommendationItems;
    private RecommendationCardView.WishlistListener listener;
    private String title;

    public WishlistRecommendationCarouselViewModel(List<RecommendationItem> recommendationItems, String title, RecommendationCardView.WishlistListener listener) {
        this.recommendationItems = recommendationItems;
        this.title = title;
        this.listener = listener;
    }

    public List<RecommendationItem> getRecommendationItems() {
        return recommendationItems;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RecommendationCardView.WishlistListener getListener() {
        return listener;
    }

    @Override
    public int type(WishlistTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
