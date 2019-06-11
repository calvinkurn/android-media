package com.tokopedia.navigation.domain.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kotlin.model.ImpressHolder;
import com.tokopedia.navigation.presentation.adapter.InboxTypeFactory;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;

/**
 * Author errysuprayogi on 13,March,2019
 */
public class Recomendation implements Visitable<InboxTypeFactory> {
    private RecommendationItem recommendationItem;

    public Recomendation(RecommendationItem recommendationItem) {
        this.recommendationItem = recommendationItem;
    }

    public RecommendationItem getRecommendationItem() {
        return recommendationItem;
    }

    public void setRecommendationItem(RecommendationItem recommendationItem) {
        this.recommendationItem = recommendationItem;
    }

    @Override
    public int type(InboxTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
