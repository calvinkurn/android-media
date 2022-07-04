package com.tokopedia.review.feature.createreputation.presentation.uimodel

import java.io.Serializable

sealed interface CreateReviewMediaUploadResult : Serializable {
    data class Success(
        val uploadId: String,
        val videoUrl: String
    ) : CreateReviewMediaUploadResult
    data class Error(
        val message: String
    ) : CreateReviewMediaUploadResult
}