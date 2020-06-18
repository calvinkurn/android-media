package com.tokopedia.reviewseller.feature.reviewdetail.view.model

data class RatingBarUiModel(
        var ratingLabel: Int? = 0,
        var ratingCount: Int = 500,
        var ratingProgressBar: Float = 0F,
        var ratingIsChecked :Boolean = false
)