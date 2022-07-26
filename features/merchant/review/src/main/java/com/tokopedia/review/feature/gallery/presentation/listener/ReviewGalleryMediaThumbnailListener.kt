package com.tokopedia.review.feature.gallery.presentation.listener

import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryImageThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryMediaThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryVideoThumbnailUiModel

interface ReviewGalleryMediaThumbnailListener {
    fun onThumbnailClicked(reviewGalleryMediaThumbnailUiModel: ReviewGalleryMediaThumbnailUiModel)
    fun onImageImpressed(element: ReviewGalleryImageThumbnailUiModel)
    fun onVideoImpressed(element: ReviewGalleryVideoThumbnailUiModel)
}