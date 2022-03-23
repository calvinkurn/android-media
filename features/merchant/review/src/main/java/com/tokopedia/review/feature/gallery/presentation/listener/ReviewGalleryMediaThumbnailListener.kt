package com.tokopedia.review.feature.gallery.presentation.listener

import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryMediaThumbnailUiModel

interface ReviewGalleryMediaThumbnailListener {
    fun onThumbnailClicked(reviewGalleryMediaThumbnailUiModel: ReviewGalleryMediaThumbnailUiModel)
}