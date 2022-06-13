package com.tokopedia.review.feature.gallery.presentation.adapter

import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryImageThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryVideoThumbnailUiModel

interface ReviewGalleryTypeFactory {
    fun type(reviewGalleryImageThumbnailUiModel: ReviewGalleryImageThumbnailUiModel): Int
    fun type(reviewGalleryVideoThumbnailUiModel: ReviewGalleryVideoThumbnailUiModel): Int
}