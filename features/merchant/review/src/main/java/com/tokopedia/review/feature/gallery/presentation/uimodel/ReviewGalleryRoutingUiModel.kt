package com.tokopedia.review.feature.gallery.presentation.uimodel

import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryUiModel

data class ReviewGalleryRoutingUiModel(
    val productId: String = "",
    val page: Int = 0,
    val totalImageCount: Long = 0L,
    val loadedReviews: MutableList<ReviewGalleryUiModel> = mutableListOf(),
    val currentPosition: Int = 0,
    val shopId: String = ""
) {

    fun getSelectedReview(position: Int = currentPosition): ReviewGalleryUiModel? {
        return loadedReviews.firstOrNull { it.imageNumber == position }
    }
}
