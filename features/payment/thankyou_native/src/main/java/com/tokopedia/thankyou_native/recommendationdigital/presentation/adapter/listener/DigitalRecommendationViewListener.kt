package com.tokopedia.thankyou_native.recommendationdigital.presentation.adapter.listener

import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationItem

interface DigitalRecommendationViewListener  {
    fun onDigitalProductClick(item: RecommendationItem, position: Int)
    fun onDigitalProductImpression(item: RecommendationItem, position: Int)

}
