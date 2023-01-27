package com.tokopedia.review.feature.bulk_write_review.domain.model

/**
 * An interface representing the state of BulkReviewSubmitUseCase. If you're adding new child of the
 * BulkReviewSubmitRequestState, please register it on the Gson provider (ex: Inside the BulkReviewModule).
 */
sealed interface BulkReviewSubmitRequestState {
    val type: String

    data class Requesting(
        override val type: String = Requesting::class.java.simpleName
    ) : BulkReviewSubmitRequestState

    sealed interface Complete : BulkReviewSubmitRequestState {
        data class Success(
            val params: List<BulkReviewSubmitRequestParam>,
            val result: BulkReviewSubmitResponse.Data.ProductRevBulkSubmitProductReview
        ) : Complete {
            override val type: String = Success::class.java.simpleName
        }

        data class Error(val throwable: Throwable?) : Complete {
            override val type: String = Error::class.java.simpleName
        }
    }
}
