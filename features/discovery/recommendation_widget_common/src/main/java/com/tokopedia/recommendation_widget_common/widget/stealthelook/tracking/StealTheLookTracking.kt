package com.tokopedia.recommendation_widget_common.widget.stealthelook.tracking

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.stealthelook.StealTheLookGridModel
import com.tokopedia.recommendation_widget_common.widget.stealthelook.StealTheLookStyleModel
import com.tokopedia.trackingoptimizer.TrackingQueue

interface StealTheLookTracking {

    fun sendEventViewportImpression(model: StealTheLookStyleModel)

    fun sendEventItemClick(model: StealTheLookGridModel)

    object Factory {
        fun create(
            widget: RecommendationWidget,
            source: RecommendationWidgetSource?,
            userId: String,
        ): StealTheLookTracking? {
            return when (source) {
                is RecommendationWidgetSource.PDP ->
                    StealTheLookTrackingPDP.Factory.create(widget, source, userId)
                else -> null
            }
        }
    }
}
