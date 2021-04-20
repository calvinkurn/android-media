package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter

import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

/**
 * Created by Lukas on 05/11/20.
 */
interface RecommendationCarouselListener : RecommendationListener{
    fun onSeeMoreCardClick(applink: String)
}