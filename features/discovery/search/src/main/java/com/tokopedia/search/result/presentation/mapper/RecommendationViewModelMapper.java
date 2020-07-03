package com.tokopedia.search.result.presentation.mapper;

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecommendationViewModelMapper {

    public List<RecommendationItemViewModel> convertToRecommendationItemViewModel(RecommendationWidget recommendationWidget) {
        List<RecommendationItemViewModel> recommendationItemViewModels = new ArrayList<>();
        for (RecommendationItem recommendationItem : recommendationWidget.getRecommendationItemList()){
            recommendationItemViewModels.add(new RecommendationItemViewModel(recommendationItem));
        }
        return recommendationItemViewModels;
    }
}
