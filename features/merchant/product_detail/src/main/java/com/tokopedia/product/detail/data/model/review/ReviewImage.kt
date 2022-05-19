package com.tokopedia.product.detail.data.model.review

import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

data class ReviewImage(
    val reviewMediaThumbnails: ReviewMediaThumbnailUiModel? = null,
    val buyerMediaCount: Int = 0,
    val staticSocialProofText: String = ""
)
