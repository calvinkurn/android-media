package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.reviewcommon.uimodel.StringRes

sealed interface CreateReviewMediaPickerUiState {
    object Loading : CreateReviewMediaPickerUiState

    data class Uploading(
        val mediaItems: List<CreateReviewMediaUiModel>,
        val poem: StringRes,
        val currentUploadBatchNumber: Int
    ) : CreateReviewMediaPickerUiState

    data class SuccessUpload(
        val mediaItems: List<CreateReviewMediaUiModel>,
        val poem: StringRes
    ) : CreateReviewMediaPickerUiState

    data class FailedUpload(
        val mediaItems: List<CreateReviewMediaUiModel>
    ) : CreateReviewMediaPickerUiState
}