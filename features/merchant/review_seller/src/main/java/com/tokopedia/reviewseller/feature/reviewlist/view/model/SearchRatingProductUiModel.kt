package com.tokopedia.reviewseller.feature.reviewlist.view.model

import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory

class SearchRatingProductUiModel: BaseReviewProduct {
    override fun type(typeFactory: SellerReviewListTypeFactory): Int {
        return typeFactory.type(this)
    }
}