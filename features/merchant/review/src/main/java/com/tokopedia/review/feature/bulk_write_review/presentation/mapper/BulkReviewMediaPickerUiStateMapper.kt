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
                mapOf(
                    *getFormRequestState.result.reviewForm.map { reviewForm ->
                        val inboxID = reviewForm.inboxID
                        val reviewItemMediaItems = mediaItems[inboxID]
                        if (reviewItemMediaItems.isNullOrEmpty()) {
                            inboxID to CreateReviewMediaPickerUiState.Hidden
                        } else {
                            val currentMediaPickerUiState = currentMediaPickerUiStates[inboxID]
                            val reviewItemPoem = poem[inboxID]?.second ?: StringRes(Int.ZERO)
                            val reviewItemUploadBatchNumber = reviewItemsUploadBatchNumber.find {
                                it.inboxID == inboxID
                            }?.batchNumber.orZero()
                            inboxID to if (reviewItemMediaItems.any { it.state == CreateReviewMediaUiModel.State.UPLOADING }) {
                                if (currentMediaPickerUiState is CreateReviewMediaPickerUiState.Uploading) {
                                    currentMediaPickerUiState.copy(
                                        mediaItems = reviewItemMediaItems,
                                        poem = reviewItemPoem,
                                        currentUploadBatchNumber = reviewItemUploadBatchNumber
                                    )
                                } else {
                                    CreateReviewMediaPickerUiState.Uploading(
                                        failedOccurrenceCount = currentMediaPickerUiState?.failedOccurrenceCount.orZero(),
                                        mediaItems = reviewItemMediaItems,
                                        poem = reviewItemPoem,
                                        currentUploadBatchNumber = reviewItemUploadBatchNumber
                                    )
                                }
                            } else if (reviewItemMediaItems.any { it.state == CreateReviewMediaUiModel.State.UPLOAD_FAILED }) {
                                val concatenatedErrorMessage = reviewItemMediaItems.filter {
                                    it.state == CreateReviewMediaUiModel.State.UPLOAD_FAILED
                                }.joinToString("|") {
                                    String.format(
                                        ReviewConstants.MEDIA_UPLOAD_ERROR_MESSAGE,
                                        it.uri,
                                        it.message
                                    )
                                }
                                val errorCode = ErrorHandler.getErrorMessagePair(
                                    context = null,
                                    e = MessageErrorException(concatenatedErrorMessage),
                                    builder = ErrorHandler.Builder()
                                ).second
                                if (currentMediaPickerUiState is CreateReviewMediaPickerUiState.FailedUpload) {
                                    currentMediaPickerUiState.copy(
                                        mediaItems = reviewItemMediaItems,
                                        shouldQueueToaster = false
                                    )
                                } else {
                                    if (currentMediaPickerUiState?.failedOccurrenceCount.isMoreThanZero()) {
                                        CreateReviewMediaPickerUiState.FailedUpload(
                                            failedOccurrenceCount = currentMediaPickerUiState?.failedOccurrenceCount.orZero()
                                                .inc(),
                                            mediaItems = reviewItemMediaItems,
                                            errorCode = errorCode,
                                            shouldQueueToaster = true
                                        )
                                    } else {
                                        CreateReviewMediaPickerUiState.FailedUpload(
                                            failedOccurrenceCount = currentMediaPickerUiState?.failedOccurrenceCount.orZero()
                                                .inc(),
                                            mediaItems = reviewItemMediaItems,
                                            errorCode = errorCode,
                                            shouldQueueToaster = false
                                        )
                                    }
                                }
                            } else {
                                CreateReviewMediaPickerUiState.SuccessUpload(
                                    mediaItems = reviewItemMediaItems,
                                    poem = reviewItemPoem
                                )
                            }
                        }
                    }.toTypedArray()
                )
            }
            else -> emptyMap()
        }
    }
}
