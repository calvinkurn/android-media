package com.tokopedia.review.feature.reviewdetail.view.model

import com.tokopedia.review.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.review.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory

data class OverallRatingDetailUiModel(
    var productName: String? = "",
    var ratingAvg: Float? = 0.0F,
    var reviewCount: Int? = 0,
    var chipFilter: String? = "",
    var tickerText: String? = ""
): BaseSellerReviewDetail {
    override fun type(typeFactory: SellerReviewDetailAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
