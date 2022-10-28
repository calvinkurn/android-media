package com.tokopedia.thankyou_native.recommendation.data


data class ProductRecommendationData(
        val title: String,
        val subtitle: String?,
        val maxHeight : Int,
        var thankYouProductCardModelList: List<ThankYouProductCardModel>
)