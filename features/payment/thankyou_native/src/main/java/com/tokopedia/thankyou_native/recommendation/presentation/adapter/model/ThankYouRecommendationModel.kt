package com.tokopedia.thankyou_native.recommendation.presentation.adapter.model

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class ThankYouRecommendationModel(
        val recommendationItem: RecommendationItem,
        val productCardModel: ProductCardModel,
        var isSeenOnceByUser: Boolean = false
)