package com.tokopedia.review.feature.createreputation.presentation.uimodel

import java.io.Serializable

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
