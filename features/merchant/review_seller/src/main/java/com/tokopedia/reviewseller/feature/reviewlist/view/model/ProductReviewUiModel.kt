package com.tokopedia.reviewseller.feature.reviewlist.view.model

import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory

data class ProductReviewUiModel(
        var productID: Int? = 0,
        var productImageUrl: String? = "",
        var productName: String? = "",
        var rating: Float? = 0.0F,
        var reviewCount: Int? = 0
): BaseReviewProduct {
    override fun type(typeFactory: SellerReviewListTypeFactory): Int {
        return typeFactory.type(this)
    }
}