package com.tokopedia.reviewseller.feature.reviewdetail.view.model

data class OverallRatingDetailUiModel(
    var productName: String? = "",
    var ratingAvg: Float? = 0.0F,
    var ratingCount: Int? = -1
)
