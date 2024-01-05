package com.tokopedia.recommendation_widget_common.widget.carousel.global.tracking

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetSource
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel
import com.tokopedia.trackingoptimizer.TrackingQueue

interface RecommendationCarouselWidgetTracking {

    fun sendEventItemImpression(
        trackingQueue: TrackingQueue,
        item: RecommendationItem,
    )

    fun sendEventItemClick(item: RecommendationItem)

    fun sendEventAddToCart(atcTrackingData: RecommendationCarouselWidgetTrackingATC)

    fun sendEventUpdateCart()

    fun sendEventDeleteCart()

    fun sendEventSeeAll()

    object Factory {
        fun create(
            widget: RecommendationWidget,
            source: RecommendationWidgetSource?,
        ): RecommendationCarouselWidgetTracking? {
            return when (source) {
                is RecommendationWidgetSource.PDPAfterATC ->
                    RecommendationCarouselWidgetTrackingPDPATC.Factory.create(widget, source)
                else -> null
            }
        }
    }
}
