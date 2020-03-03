package com.tokopedia.reviewseller.feature.reviewlist.view.model

import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory

data class ProductReviewModel(
        val title: String? = null,
        val image: String? = null,
        val rating: String? = null,
        val review: String? = null
): BaseReviewProduct {
    override fun type(typeFactory: SellerReviewListTypeFactory): Int {
        return typeFactory.type(this)
    }
}