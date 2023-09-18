package com.tokopedia.recommendation_widget_common.widget.header

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.global.tracking.RecommendationCarouselWidgetTracking

interface RecommendationHeaderListener {
    fun onSeeAllClick(link: String, tracking: RecommendationCarouselWidgetTracking?)
    fun onChannelExpired(widget: RecommendationWidget)
}
