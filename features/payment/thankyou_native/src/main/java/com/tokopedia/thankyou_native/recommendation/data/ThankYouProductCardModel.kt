package com.tokopedia.thankyou_native.recommendation.data

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class ThankYouProductCardModel(
        val recommendationItem: RecommendationItem,
        val productCardModel: ProductCardModel,
        var isSeenOnceByUser: Boolean = false
)