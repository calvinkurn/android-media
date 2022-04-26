package com.tokopedia.product.detail.data.model.review

import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

data class ReviewImage(
        val reviewMediaThumbnails: ReviewMediaThumbnailUiModel? = null,
        val detailedMediaResult: ProductrevGetReviewMedia? = null,
        val buyerPhotosCount: Int = 0,
        val staticSocialProofText: String = ""
)
