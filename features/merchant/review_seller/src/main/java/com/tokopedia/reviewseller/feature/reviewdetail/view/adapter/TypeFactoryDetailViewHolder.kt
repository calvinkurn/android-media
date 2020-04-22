package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter

import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.OverallRatingDetailUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.TopicUiModel

interface TypeFactoryDetailViewHolder {
    fun type(feedbackUiModel: FeedbackUiModel): Int
    fun type(overallRatingDetailUiModel: OverallRatingDetailUiModel): Int
    fun type(ratingBarUiModel: RatingBarUiModel): Int
    fun type(topicUiModel: TopicUiModel): Int
}