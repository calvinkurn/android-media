package com.tokopedia.gamification.pdp.presentation

import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface GamiPdpRecommendationListener : RecommendationListener {
    fun onProductImpression(item: RecommendationItem, position:Int)
}