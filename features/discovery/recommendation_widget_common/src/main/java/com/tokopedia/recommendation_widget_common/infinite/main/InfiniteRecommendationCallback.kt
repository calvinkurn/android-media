package com.tokopedia.recommendation_widget_common.infinite.main

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

interface InfiniteRecommendationCallback {
    fun fetchRecommendation()
    fun onClickProductCard(
        recommendationItem: RecommendationItem,
        additionalAppLogParams: Map<String, Any>
    )
    fun onImpressProductCard(
        recommendationItem: RecommendationItem,
        additionalAppLogParams: Map<String, Any>,
    )
    fun onClickViewAll(recommendationWidget: RecommendationWidget)
}
