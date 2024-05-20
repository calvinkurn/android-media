package com.tokopedia.recommendation_widget_common.infinite.main

import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

interface InfiniteRecommendationCallback {
    fun fetchRecommendation()
    fun onClickProductCard(
        recommendationItem: RecommendationItem,
        additionalAppLogParam: AppLogAdditionalParam,
    )
    fun onImpressProductCard(
        recommendationItem: RecommendationItem,
        additionalAppLogParam: AppLogAdditionalParam,
    )
    fun onClickViewAll(recommendationWidget: RecommendationWidget)
}
