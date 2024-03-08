package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class RecommendationWidgetModel(
    val metadata: RecommendationWidgetMetadata = RecommendationWidgetMetadata(),
    @Deprecated("Please use source for page-specific trackers.")
    val trackingModel: RecommendationWidgetTrackingModel = RecommendationWidgetTrackingModel(),
    val miniCart: RecommendationWidgetMiniCart = RecommendationWidgetMiniCart(),
    val source: RecommendationWidgetSource? = null,
    val listener: RecommendationWidgetListener? = null,

    // TEMPORARY, Ignore this for non-PDP
    val widget: RecommendationWidget? = null,
) {

    val id: String
        get() = metadata.pageName
}
