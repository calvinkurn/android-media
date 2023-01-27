package com.tokopedia.review.feature.bulk_write_review.domain.model

import com.tokopedia.review.feature.createreputation.model.BadRatingCategoriesResponse

/**
 * An interface representing the state of BulkReviewGetBadRatingCategoryUseCase. If you're adding
 * new child of the BulkReviewGetBadRatingCategoryRequestState, please register it on the Gson
 * provider (ex: Inside the BulkReviewModule).
 */
sealed interface BulkReviewGetBadRatingCategoryRequestState {
    val type: String

    data class Requesting(
        override val type: String = Requesting::class.java.simpleName
    ) : BulkReviewGetBadRatingCategoryRequestState

    sealed interface Complete : BulkReviewGetBadRatingCategoryRequestState {
        data class Success(val result: BadRatingCategoriesResponse) : Complete {
            override val type: String = Success::class.java.simpleName
        }

        data class Error(val throwable: Throwable?) : Complete {
            override val type: String = Error::class.java.simpleName
        }
    }
}
