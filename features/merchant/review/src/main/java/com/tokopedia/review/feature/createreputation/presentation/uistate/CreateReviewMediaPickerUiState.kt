package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.reviewcommon.uimodel.StringRes

sealed interface CreateReviewMediaPickerUiState {

    interface HasMedia {
        val mediaItems: List<CreateReviewMediaUiModel>

        fun getImageCount(): Int {
            return mediaItems.count { it is CreateReviewMediaUiModel.Image }
        }

        fun getVideoCount(): Int {
            return mediaItems.count { it is CreateReviewMediaUiModel.Video }
        }
    }

    val failedOccurrenceCount: Int

    object Loading : CreateReviewMediaPickerUiState {
        override val failedOccurrenceCount: Int
            get() = Int.ZERO
    }

    object Hidden : CreateReviewMediaPickerUiState {
        override val failedOccurrenceCount: Int
            get() = Int.ZERO
    }

    data class Uploading(
        override val failedOccurrenceCount: Int,
        override val mediaItems: List<CreateReviewMediaUiModel>,
        val poem: StringRes,
        val currentUploadBatchNumber: Int
    ) : CreateReviewMediaPickerUiState, HasMedia

    data class SuccessUpload(
        override val failedOccurrenceCount: Int = 0,
        override val mediaItems: List<CreateReviewMediaUiModel>,
        val poem: StringRes
    ) : CreateReviewMediaPickerUiState, HasMedia

    data class FailedUpload(
        override val failedOccurrenceCount: Int,
        override val mediaItems: List<CreateReviewMediaUiModel>,
        val errorCode: String,
        val shouldQueueToaster: Boolean
    ) : CreateReviewMediaPickerUiState, HasMedia
}
