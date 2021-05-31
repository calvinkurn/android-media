package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.search.result.presentation.model.RecommendationItemDataView

class RecommendationViewModelMapper {

    fun convertToRecommendationItemViewModel(recommendationWidget: RecommendationWidget?): List<RecommendationItemDataView> {
        return recommendationWidget?.recommendationItemList?.map { RecommendationItemDataView(it) } ?: listOf()
    }
}