package com.tokopedia.thankyou_native.recommendation.model

import com.tokopedia.productcard.v2.BlankSpaceConfig

data class MarketPlaceRecommendationResult(
        val title: String,
        val marketPlaceRecommendationModelList: List<MarketPlaceRecommendationModel>,
        val blankSpaceConfig: BlankSpaceConfig
)