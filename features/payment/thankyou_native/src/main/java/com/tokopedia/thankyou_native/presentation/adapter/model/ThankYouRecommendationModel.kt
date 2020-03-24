package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class ThankYouRecommendationModel(
        val recommendationItem: RecommendationItem,
        val productCardModel: ProductCardModel
)