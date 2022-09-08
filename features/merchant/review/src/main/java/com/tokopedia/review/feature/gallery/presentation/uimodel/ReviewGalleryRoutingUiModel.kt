package com.tokopedia.review.feature.gallery.presentation.uimodel

import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryImageThumbnailUiModel

data class ReviewGalleryRoutingUiModel(
    val productId: String = "",
    val page: Int = 0,
    val totalImageCount: Long = 0L,
    val loadedReviews: MutableList<ReviewGalleryImageThumbnailUiModel> = mutableListOf(),
    val currentPosition: Int = 0,
    val shopId: String = ""
) {

    fun getSelectedReview(position: Int = currentPosition): ReviewGalleryImageThumbnailUiModel? {
        return loadedReviews.firstOrNull { it.mediaNumber == position }
    }
}
