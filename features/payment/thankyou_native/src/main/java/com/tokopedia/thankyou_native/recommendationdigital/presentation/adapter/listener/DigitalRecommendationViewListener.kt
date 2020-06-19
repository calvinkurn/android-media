package com.tokopedia.thankyou_native.recommendationdigital.presentation.adapter.listener

import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationsItem

interface DigitalRecommendationViewListener  {
    fun onDigitalProductClick(item: RecommendationsItem, position: Int)
    fun onDigitalProductImpression(item: RecommendationsItem, position: Int)

}
