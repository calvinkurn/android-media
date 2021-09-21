package com.tokopedia.tokopoints.view.tokopointhome

import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

interface RewardsRecomListener : RecommendationListener {
    fun onProductImpression(item: RecommendationItem, position:Int)
}