package com.tokopedia.reviewseller.feature.reviewlist.view.model

import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory

data class ProductRatingOverallModel(
        var rating: Float? = 0.0F,
        var reviewCount: Int? = -1,
        var period: String? = ""
): BaseReviewProduct {
    override fun type(typeFactory: SellerReviewListTypeFactory): Int {
        return typeFactory.type(this)
    }
}