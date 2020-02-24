package com.tokopedia.reviewseller.feature.reviewdetail.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.RatingBarAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory

data class RatingBarModel(
    var ratingBarAdapter: RatingBarAdapter
): Visitable<SellerReviewDetailAdapterTypeFactory> {
    override fun type(typeFactory: SellerReviewDetailAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}