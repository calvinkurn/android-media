package com.tokopedia.review.feature.reviewdetail.view.model

data class ProductFeedbackDetailUiModel(
        var ratingBarList: List<RatingBarUiModel> = listOf(),
        var productFeedbackDetailList: List<FeedbackUiModel> = listOf(),
        var page: Int = 0,
        var hasNext: Boolean = false,
        var reviewCount: Long = 0
)