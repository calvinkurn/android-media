package com.tokopedia.review.feature.gallery.presentation.uimodel

data class SelectedReview(
    val imageUrl: String = "",
    val reviewerName: String = "",
    val rating: Int = 0,
    val isLiked: Boolean = false,
    val totalLiked: Long = 0L,
    val review: String = "",
    val reviewTime: String = "",
    val isReportable: Boolean = false
)