package com.tokopedia.review.feature.createreputation.presentation.uimodel

data class CreateReviewProgressBarState(
        var isGoodRating: Boolean = false,
        var isPhotosFilled: Boolean = false,
        var isTextAreaFilled: Boolean = false,
        var isBadRatingReasonSelected: Boolean = false
) {
    fun isComplete(): Boolean = isPhotosFilled && isTextAreaFilled
    fun isNeedPhoto(): Boolean = isTextAreaFilled && !isPhotosFilled
    fun isNeedReview(): Boolean = isPhotosFilled && !isTextAreaFilled
    fun isNeedBadRating(): Boolean = !isBadRatingReasonSelected && !isGoodRating
}