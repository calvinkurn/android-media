package com.tokopedia.review.feature.reviewdetail.view.model

import com.tokopedia.review.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.review.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory

/**
 * Created by Yehezkiel on 28/04/20
 */
data class ProductReviewFilterUiModel(
        var ratingBarList: List<RatingBarUiModel> = listOf(),
        var topicList: TopicUiModel = TopicUiModel()
) : BaseSellerReviewDetail {

    override fun type(typeFactory: SellerReviewDetailAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}