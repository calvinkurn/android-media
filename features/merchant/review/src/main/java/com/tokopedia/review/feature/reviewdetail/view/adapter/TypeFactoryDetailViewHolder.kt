package com.tokopedia.review.feature.reviewdetail.view.adapter

import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.OverallRatingDetailUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.ProductFeedbackErrorUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.ProductReviewFilterUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.TopicUiModel

interface TypeFactoryDetailViewHolder {
    fun type(feedbackUiModel: FeedbackUiModel): Int
    fun type(overallRatingDetailUiModel: OverallRatingDetailUiModel): Int
    fun type(topicUiModel: TopicUiModel): Int
    fun type(filterUiModel: ProductReviewFilterUiModel): Int
    fun type(feedbackErrorUiModel: ProductFeedbackErrorUiModel): Int
}