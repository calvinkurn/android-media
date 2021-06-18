package com.tokopedia.review.feature.reading.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewAdapterTypeFactory

data class ReadReviewUiModel(
        val reviewData: ProductReview,
        val isShopViewHolder: Boolean,
        val shopId: String,
        val shopName: String
) : Visitable<ReadReviewAdapterTypeFactory> {

    override fun type(typeFactory: ReadReviewAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

