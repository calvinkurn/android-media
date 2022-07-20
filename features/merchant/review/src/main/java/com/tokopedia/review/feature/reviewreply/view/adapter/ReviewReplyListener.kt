package com.tokopedia.review.feature.reviewreply.view.adapter

import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

interface ReviewReplyListener {
    fun onImageItemClicked(
        reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel,
        title: String,
        feedbackId: String,
        productID: String,
        position: Int
    )
}