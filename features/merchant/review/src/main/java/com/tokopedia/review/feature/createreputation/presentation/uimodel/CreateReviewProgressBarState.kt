package com.tokopedia.review.feature.createreputation.presentation.uimodel

data class CreateReviewProgressBarState(
    val isGoodRating: Boolean = false,
    val isPhotosFilled: Boolean = false,
    val isTestimonyComplete: Boolean = false,
    val isBadRatingReasonSelected: Boolean = false
) {
    // New Flow
    fun isComplete(): Boolean {
        return if (isGoodRating) isPhotosFilled && isTestimonyComplete else isBadRatingReasonSelected && isTestimonyComplete && isPhotosFilled
    }

    fun isNeedPhotoOnly(): Boolean {
        return if (isGoodRating) !isPhotosFilled && isTestimonyComplete else isBadRatingReasonSelected && isTestimonyComplete && !isPhotosFilled
    }

    fun isNeedReviewOnly(): Boolean {
        return if (isGoodRating) isPhotosFilled && !isTestimonyComplete else isBadRatingReasonSelected && !isTestimonyComplete && isPhotosFilled
    }

    fun isNeedBadRatingReasonOnly(): Boolean {
        return !isBadRatingReasonSelected && !isGoodRating && (isPhotosFilled || isTestimonyComplete)
    }
}
