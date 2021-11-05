package com.tokopedia.review.feature.createreputation.presentation.uimodel

data class CreateReviewProgressBarState(
    var isGoodRating: Boolean = false,
    var isPhotosFilled: Boolean = false,
    var isTextAreaFilled: Boolean = false,
    var isBadRatingReasonSelected: Boolean = false
) {
    // New Flow
    fun isComplete(): Boolean {
        return if (isGoodRating) isPhotosFilled && isTextAreaFilled else isBadRatingReasonSelected && isTextAreaFilled && isPhotosFilled
    }

    fun isNeedPhotoOnly(): Boolean {
        return if (isGoodRating) !isPhotosFilled && isTextAreaFilled else isBadRatingReasonSelected && isTextAreaFilled && !isPhotosFilled
    }

    fun isNeedReviewOnly(): Boolean {
        return if (isGoodRating) isPhotosFilled && !isTextAreaFilled else isBadRatingReasonSelected && !isTextAreaFilled && isPhotosFilled
    }

    fun isNeedBadRatingReasonOnly(): Boolean {
        return !isBadRatingReasonSelected && !isGoodRating && (isPhotosFilled || isTextAreaFilled)
    }

    fun getProgressCount(): Int {
        if (isGoodRating) {
            return (listOf(isPhotosFilled, isTextAreaFilled).count { it } + 1) * 100
        }
        return (listOf(
            isPhotosFilled,
            isTextAreaFilled,
            isBadRatingReasonSelected
        ).count { it } + 1) * 100
    }

    // Old Flow
    fun isCompleteOldFlow(): Boolean = isPhotosFilled && isTextAreaFilled
    fun isNeedPhotoOldFlow(): Boolean = isTextAreaFilled && !isPhotosFilled
    fun isNeedReviewOldFlow(): Boolean = isPhotosFilled && !isTextAreaFilled
}