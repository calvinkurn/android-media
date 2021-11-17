package com.tokopedia.review.feature.reviewdetail.view.model

import com.tokopedia.review.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.review.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory

data class BadRatingReasonTickerUiModel(
    var tickerText: String = ""
): BaseSellerReviewDetail {
    override fun type(typeFactory: SellerReviewDetailAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}