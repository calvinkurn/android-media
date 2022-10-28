package com.tokopedia.review.feature.reading.presentation.listener

import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

interface ReadReviewAttachedImagesListener {
    fun onAttachedImagesClicked(
        productReview: ProductReview,
        reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel,
        positionClicked: Int,
        shopId: String,
        reviewItemPosition: Int
    )
}