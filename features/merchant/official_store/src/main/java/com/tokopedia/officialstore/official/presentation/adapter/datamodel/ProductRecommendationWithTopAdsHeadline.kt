package com.tokopedia.officialstore.official.presentation.adapter.datamodel

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class ProductRecommendationWithTopAdsHeadline(
    val recommendationWidget: RecommendationWidget,
    val officialTopAdsHeadlineDataModel: OfficialTopAdsHeadlineDataModel?
)
