package com.tokopedia.recommendation_widget_common.infinite.main

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

interface InfiniteRecommendationListener {
    fun onClickProductCard(recommendationItem: RecommendationItem)
    fun onImpressProductCard(recommendationItem: RecommendationItem)
    fun onClickViewAll(recommendationWidget: RecommendationWidget)
}
