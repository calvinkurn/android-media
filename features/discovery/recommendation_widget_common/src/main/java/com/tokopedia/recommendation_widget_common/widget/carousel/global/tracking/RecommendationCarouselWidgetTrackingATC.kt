package com.tokopedia.recommendation_widget_common.widget.carousel.global.tracking

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class RecommendationCarouselWidgetTrackingATC(
    val item: RecommendationItem,
    val cartId: String,
    val quantity: Int,
)
