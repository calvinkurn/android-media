package com.tokopedia.thankyou_native.recommendationdigital.presentation.adapter.listener

import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationItem
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationsItem

interface DigitalRecommendationViewListener  {
    fun onDigitalProductClick(item: RecommendationItem, position: Int)
    fun onDigitalProductImpression(item: RecommendationItem, position: Int)

}
