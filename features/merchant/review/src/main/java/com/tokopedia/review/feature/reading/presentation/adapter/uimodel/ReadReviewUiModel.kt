package com.tokopedia.review.feature.reading.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewAdapterTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

data class ReadReviewUiModel(
        val reviewData: ProductReview,
        val isShopViewHolder: Boolean,
        val shopId: String,
        val shopName: String,
        val productImage: String = "",
        val productName: String = "",
        val productId: String = "",
        val mediaThumbnails: ReviewMediaThumbnailUiModel = ReviewMediaThumbnailUiModel(),
        val impressHolder: ImpressHolder = ImpressHolder()
) : Visitable<ReadReviewAdapterTypeFactory> {

    override fun type(typeFactory: ReadReviewAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

