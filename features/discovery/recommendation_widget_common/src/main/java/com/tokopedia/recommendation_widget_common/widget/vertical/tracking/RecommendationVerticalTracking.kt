package com.tokopedia.recommendation_widget_common.widget.vertical.tracking

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel
import com.tokopedia.trackingoptimizer.TrackingQueue

interface RecommendationVerticalTracking {

    fun sendEventItemImpression(
        trackingQueue: TrackingQueue,
        item: RecommendationItem,
    )

    fun sendEventItemClick(item: RecommendationItem)

    fun sendEventSeeMoreClick()


    object Factory {
        fun create(
            widget: RecommendationWidget,
            source: RecommendationWidgetSource?,
            userId: String,
        ): RecommendationVerticalTracking? {
            return when (source) {
                is RecommendationWidgetSource.PDP ->
                    RecommendationVerticalTrackingPDP.Factory.create(widget, source, userId)
                else -> null
            }
        }
    }
}
