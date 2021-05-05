package com.tokopedia.review.feature.createreputation.presentation.uimodel

data class CreateReviewProgressBarState(
        var isGoodRating: Boolean,
        var isPhotosFilled: Boolean,
        var isTextAreaFilled: Boolean
) {
    fun isComplete(): Boolean = isPhotosFilled && isTextAreaFilled
    fun isNeedPhoto(): Boolean = isTextAreaFilled && !isPhotosFilled
    fun isNeedReview(): Boolean = isPhotosFilled && !isTextAreaFilled
}