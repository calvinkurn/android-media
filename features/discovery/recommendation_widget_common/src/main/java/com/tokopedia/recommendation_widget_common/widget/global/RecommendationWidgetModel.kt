package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class RecommendationWidgetModel(
    val metadata: RecommendationWidgetMetadata = RecommendationWidgetMetadata(),
    val trackingModel: RecommendationWidgetTrackingModel = RecommendationWidgetTrackingModel(),
    val miniCart: RecommendationWidgetMiniCart = RecommendationWidgetMiniCart(),
    val source: RecommendationWidgetSource? = null,

    // TEMPORARY, Ignore this for non-PDP
    val widget: RecommendationWidget? = null,
) {

    val id: String
        get() = metadata.pageName
}
