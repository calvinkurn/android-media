package com.tokopedia.recommendation_widget_common.widget.header

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

interface RecommendationHeaderListener {
    fun onSeeAllClick(link: String)
    fun onChannelExpired(widget: RecommendationWidget)
}