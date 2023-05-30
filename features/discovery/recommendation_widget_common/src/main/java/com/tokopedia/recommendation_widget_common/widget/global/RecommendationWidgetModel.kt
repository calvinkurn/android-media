package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class RecommendationWidgetModel(
    val metadata: RecommendationWidgetMetadata = RecommendationWidgetMetadata(),
    val trackingModel: RecommendationWidgetTrackingModel = RecommendationWidgetTrackingModel(),

    // TEMPORARY
    val widget: RecommendationWidget? = null,
) {

    val id: String
        get() = metadata.pageName
}
