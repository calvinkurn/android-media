package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemMediaUploadBatchNumberUiModel
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import com.tokopedia.reviewcommon.uimodel.StringRes
import javax.inject.Inject

class BulkReviewMediaPickerUiStateMapper @Inject constructor() {
    fun map(
        getFormRequestState: BulkReviewGetFormRequestState,
        mediaItems: Map<String, List<CreateReviewMediaUiModel>>,
        poem: Map<String, Pair<String, StringRes>>,
        currentMediaPickerUiStates: Map<String, CreateReviewMediaPickerUiState>,
        reviewItemsUploadBatchNumber: List<BulkReviewItemMediaUploadBatchNumberUiModel>
    ): Map<String, CreateReviewMediaPickerUiState> {
        return when (getFormRequestState) {
            is BulkReviewGetFormRequestState.Complete.Success -> {
                getFormRequestState.result.reviewForm.associateBy(
                    keySelector = { reviewForm ->
                        reviewForm.inboxID
                    },
                    valueTransform = { reviewForm ->
                        mapReviewFormToMediaPickerUiState(
                            mediaItems = mediaItems[reviewForm.inboxID],
                            poem = poem[reviewForm.inboxID]?.second ?: StringRes(Int.ZERO),
                            currentMediaPickerUiState = currentMediaPickerUiStates[reviewForm.inboxID],
                            reviewItemsUploadBatchNumber = reviewItemsUploadBatchNumber.find {
                                it.inboxID == reviewForm.inboxID
                            }
                        )
                    }
                )
            }
            else -> emptyMap()
        }
    }

    private fun mapReviewFormToMediaPickerUiState(
        mediaItems: List<CreateReviewMediaUiModel>?,
        poem: StringRes,
        currentMediaPickerUiState: CreateReviewMediaPickerUiState?,
        reviewItemsUploadBatchNumber: BulkReviewItemMediaUploadBatchNumberUiModel?
    ): CreateReviewMediaPickerUiState {
        return if (mediaItems.isNullOrEmpty()) {
            CreateReviewMediaPickerUiState.Hidden
        } else {
            if (mediaItems.any { it.state == CreateReviewMediaUiModel.State.UPLOADING }) {
                mapOnMediaPickerIsUploading(
                    mediaItems = mediaItems,
                    poem = poem,
                    currentMediaPickerUiState = currentMediaPickerUiState,
                    reviewItemsUploadBatchNumber = reviewItemsUploadBatchNumber
                )
            } else if (mediaItems.any { it.state == CreateReviewMediaUiModel.State.UPLOAD_FAILED }) {
                mapOnMediaPickerIsFailedUpload(
                    mediaItems = mediaItems,
                    currentMediaPickerUiState = currentMediaPickerUiState
                )
            } else {
                CreateReviewMediaPickerUiState.SuccessUpload(
                    mediaItems = mediaItems,
                    poem = poem
                )
            }
        }
    }

    private fun mapOnMediaPickerIsUploading(
        mediaItems: List<CreateReviewMediaUiModel>,
        poem: StringRes,
        currentMediaPickerUiState: CreateReviewMediaPickerUiState?,
        reviewItemsUploadBatchNumber: BulkReviewItemMediaUploadBatchNumberUiModel?
    ): CreateReviewMediaPickerUiState.Uploading {
        return if (currentMediaPickerUiState is CreateReviewMediaPickerUiState.Uploading) {
            currentMediaPickerUiState.copy(
                mediaItems = mediaItems,
                poem = poem,
                currentUploadBatchNumber = reviewItemsUploadBatchNumber?.batchNumber.orZero()
            )
        } else {
            CreateReviewMediaPickerUiState.Uploading(
                failedOccurrenceCount = currentMediaPickerUiState?.failedOccurrenceCount.orZero(),
                mediaItems = mediaItems,
                poem = poem,
                currentUploadBatchNumber = reviewItemsUploadBatchNumber?.batchNumber.orZero()
            )
        }
    }

    private fun mapOnMediaPickerIsFailedUpload(
        mediaItems: List<CreateReviewMediaUiModel>,
        currentMediaPickerUiState: CreateReviewMediaPickerUiState?
    ): CreateReviewMediaPickerUiState.FailedUpload {
        val concatenatedErrorMessage = concatMediaItemsUploadErrorMessage(mediaItems)
        val errorCode = logErrorAndGetErrorCode(concatenatedErrorMessage)
        return if (currentMediaPickerUiState is CreateReviewMediaPickerUiState.FailedUpload) {
            currentMediaPickerUiState.copy(
                mediaItems = mediaItems,
                shouldQueueToaster = false
            )
        } else {
            if (currentMediaPickerUiState?.failedOccurrenceCount.isMoreThanZero()) {
                CreateReviewMediaPickerUiState.FailedUpload(
                    failedOccurrenceCount = currentMediaPickerUiState?.failedOccurrenceCount.orZero().inc(),
                    mediaItems = mediaItems,
                    errorCode = errorCode,
                    shouldQueueToaster = true
                )
            } else {
                CreateReviewMediaPickerUiState.FailedUpload(
                    failedOccurrenceCount = currentMediaPickerUiState?.failedOccurrenceCount.orZero().inc(),
                    mediaItems = mediaItems,
                    errorCode = errorCode,
                    shouldQueueToaster = false
                )
            }
        }
    }

    private fun concatMediaItemsUploadErrorMessage(mediaItems: List<CreateReviewMediaUiModel>): String {
        return mediaItems.filter {
            it.state == CreateReviewMediaUiModel.State.UPLOAD_FAILED
        }.joinToString("|") {
            String.format(
                ReviewConstants.MEDIA_UPLOAD_ERROR_MESSAGE,
                it.uri,
                it.message
            )
        }
    }

    private fun logErrorAndGetErrorCode(concatenatedErrorMessage: String): String {
        return ErrorHandler.getErrorMessagePair(
            context = null,
            e = MessageErrorException(concatenatedErrorMessage),
            builder = ErrorHandler.Builder()
        ).second
    }
}
