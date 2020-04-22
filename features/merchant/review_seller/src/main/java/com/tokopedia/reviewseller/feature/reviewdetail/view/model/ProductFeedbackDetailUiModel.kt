package com.tokopedia.reviewseller.feature.reviewdetail.view.model


data class ProductFeedbackDetailUiModel(
        var ratingBarList: List<RatingBarUiModel> = listOf(),
        var productFeedbackDetailList: List<FeedbackUiModel> = listOf(),
        var topicList: TopicUiModel = TopicUiModel()
)