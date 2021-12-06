package com.tokopedia.review.feature.createreputation.presentation.uimodel

data class CreateReviewProgressBarState(
    var isGoodRating: Boolean = false,
    var isPhotosFilled: Boolean = false,
    var isTextAreaFilled: Boolean = false,
    var isBadRatingReasonSelected: Boolean = false
) {
    companion object {
        const val PERCENT_MULTIPLIER = 100
    }

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

    // Old Flow
    fun isCompleteOldFlow(): Boolean = isPhotosFilled && isTextAreaFilled
    fun isNeedPhotoOldFlow(): Boolean = isTextAreaFilled && !isPhotosFilled
    fun isNeedReviewOldFlow(): Boolean = isPhotosFilled && !isTextAreaFilled
}