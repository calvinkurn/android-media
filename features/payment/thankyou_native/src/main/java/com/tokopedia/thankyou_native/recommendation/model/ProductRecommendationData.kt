package com.tokopedia.thankyou_native.recommendation.model


data class ProductRecommendationData(
        val title: String,
        val maxHeight : Int,
        var thankYouProductCardModelList: List<ThankYouProductCardModel>
)