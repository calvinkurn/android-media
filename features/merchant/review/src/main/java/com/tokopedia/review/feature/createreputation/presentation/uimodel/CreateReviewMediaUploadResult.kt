package com.tokopedia.review.feature.createreputation.presentation.uimodel

import java.io.Serializable

/**
 * An interface representing the state of media upload process. If you're adding new child of the
 * CreateReviewMediaUploadResult, please register it on the Gson provider (ex: Inside the BulkReviewModule).
 */
sealed interface CreateReviewMediaUploadResult : Serializable {
    val type: String
    data class Success(
        val uploadId: String,
        val videoUrl: String
    ) : CreateReviewMediaUploadResult {
        override val type: String = Success::class.java.simpleName
    }

    data class Error(
        val message: String
    ) : CreateReviewMediaUploadResult {
        override val type: String = Error::class.java.simpleName
    }
}
