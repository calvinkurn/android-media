package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.trackingoptimizer.TrackingQueue

data class BaseRecommendationWidgetModel(
    override val metadata: RecommendationWidgetMetadata,
    override val trackingModel: RecommendationWidgetTrackingModel = RecommendationWidgetTrackingModel(),
    override val analyticListener: RecommendationWidgetAnalyticListener? = null,
    override var trackingQueue: TrackingQueue? = null
) : RecommendationVisitable {
    override fun type(typeFactory: RecommendationTypeFactory?): Int {
        return -1
    }
}
