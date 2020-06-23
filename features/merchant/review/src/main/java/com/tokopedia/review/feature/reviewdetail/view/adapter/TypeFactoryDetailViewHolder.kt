package com.tokopedia.review.feature.reviewdetail.view.adapter

import com.tokopedia.review.feature.reviewdetail.view.model.*

interface TypeFactoryDetailViewHolder {
    fun type(feedbackUiModel: FeedbackUiModel): Int
    fun type(overallRatingDetailUiModel: OverallRatingDetailUiModel): Int
    fun type(topicUiModel: TopicUiModel): Int
    fun type(filterUiModel: ProductReviewFilterUiModel): Int
    fun type(feedbackErrorUiModel: ProductFeedbackErrorUiModel): Int
}