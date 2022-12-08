package com.tokopedia.review.feature.bulk_write_review.domain.model

import com.tokopedia.review.feature.createreputation.model.BadRatingCategoriesResponse

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
