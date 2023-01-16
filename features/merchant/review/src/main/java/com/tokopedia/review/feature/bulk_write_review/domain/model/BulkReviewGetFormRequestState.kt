package com.tokopedia.review.feature.bulk_write_review.domain.model

sealed interface BulkReviewGetFormRequestState {
    val type: String

    data class Requesting(
        override val type: String = Requesting::class.java.simpleName
    ) : BulkReviewGetFormRequestState

    sealed interface Complete : BulkReviewGetFormRequestState {
        data class Success(
            val result: BulkReviewGetFormResponse.Data.ProductRevBulkSubmitProductReview
        ) : Complete {
            override val type: String = Success::class.java.simpleName
        }

        data class Error(val throwable: Throwable?) : Complete {
            override val type: String = Error::class.java.simpleName
        }
    }
}
