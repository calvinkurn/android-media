package com.tokopedia.reviewseller.common

import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory

data class SummaryReviewModel(
        val rating: String? = null,
        val review: String? = null,
        val period: String? = null
): BaseReviewProduct {
    override fun type(typeFactory: SellerReviewListTypeFactory): Int {
        return typeFactory.type(this)
    }
}