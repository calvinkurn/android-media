package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class RecommendationWidgetModel(
    val metadata: RecommendationWidgetMetadata = RecommendationWidgetMetadata(),
    @Deprecated("Please use source for page-specific trackers.")
    val trackingModel: RecommendationWidgetTrackingModel = RecommendationWidgetTrackingModel(),
    val miniCart: RecommendationWidgetMiniCart = RecommendationWidgetMiniCart(),
    @Deprecated("Deprecated usage for GA. For ByteIO use appLogAdditionalParam instead.")
    val source: RecommendationWidgetSource? = null,
    val appLogAdditionalParam: AppLogAdditionalParam = AppLogAdditionalParam.None,
    val listener: RecommendationWidgetListener? = null,

    // TEMPORARY, Ignore this for non-PDP
    val widget: RecommendationWidget? = null,
) {

    val id: String
        get() = metadata.pageName
}
