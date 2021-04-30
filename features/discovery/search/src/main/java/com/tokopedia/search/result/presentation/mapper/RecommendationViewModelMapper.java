package com.tokopedia.search.result.presentation.mapper;

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.search.result.presentation.model.RecommendationItemDataView;

import java.util.ArrayList;
import java.util.List;

public class RecommendationViewModelMapper {

    public List<RecommendationItemDataView> convertToRecommendationItemViewModel(RecommendationWidget recommendationWidget) {
        List<RecommendationItemDataView> recommendationItemDataViews = new ArrayList<>();
        for (RecommendationItem recommendationItem : recommendationWidget.getRecommendationItemList()){
            recommendationItemDataViews.add(new RecommendationItemDataView(recommendationItem));
        }
        return recommendationItemDataViews;
    }
}
