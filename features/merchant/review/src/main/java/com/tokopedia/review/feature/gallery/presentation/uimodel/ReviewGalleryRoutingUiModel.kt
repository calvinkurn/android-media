package com.tokopedia.review.feature.gallery.presentation.uimodel

data class ReviewGalleryRoutingUiModel(
    val productId: String = "",
    val page: Int = 0,
    val totalImageCount: Long = 0L,
    val selectedReview: SelectedReview = SelectedReview()
)
