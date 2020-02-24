package com.tokopedia.reviewseller.feature.reviewdetail.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory

data class HeaderModel(
    var ratingAvg: String? = "0.0",
    var totalReview: String? = "0"
): Visitable<SellerReviewDetailAdapterTypeFactory> {
    override fun type(typeFactory: SellerReviewDetailAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}