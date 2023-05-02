package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.trackingoptimizer.TrackingQueue

data class BaseRecomWidgetModel(
    override val recomWidgetMetadata: RecomWidgetMetadata,
    override val recomWidgetTrackingModel: RecomWidgetTrackingModel = RecomWidgetTrackingModel(),
    override val recomWidgetAnalyticListener: RecomWidgetAnalyticListener? = null,
    override var trackingQueue: TrackingQueue? = null
) : RecomVisitable {
    override fun type(typeFactory: RecomTypeFactory?): Int {
        return -1
    }
}
