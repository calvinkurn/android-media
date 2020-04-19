package com.tokopedia.reviewseller.feature.reviewdetail.view.model

import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory

data class ProductFeedbackDetailUiModel(
        var ratingBarList: List<RatingBarUiModel> = listOf(),
        var productFeedbackDetailList: List<FeedbackUiModel> = listOf(),
        var topicList: List<TopicUiModel> = listOf()
): BaseSellerReviewDetail{
    override fun type(typeFactory: SellerReviewDetailAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}