package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.review.R
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewMiniActionUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewMiniActionsUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewTextAreaUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import com.tokopedia.reviewcommon.uimodel.StringRes
import javax.inject.Inject

class BulkReviewMiniActionsUiStateMapper @Inject constructor() {
    fun map(
        getFormRequestState: BulkReviewGetFormRequestState,
        bulkReviewTextAreaUiState: Map<String, BulkReviewTextAreaUiState>,
        bulkReviewMediaPickerUiState: Map<String, CreateReviewMediaPickerUiState>
    ): Map<String, BulkReviewMiniActionsUiState> {
        return when (getFormRequestState) {
            is BulkReviewGetFormRequestState.Complete.Success -> {
                getFormRequestState.result.reviewForm.associateBy(
                    keySelector = { reviewForm ->
                        reviewForm.inboxID
                    },
                    valueTransform = { reviewForm ->
                        mapMiniActionsUiState(
                            reviewForm = reviewForm,
                            bulkReviewTextAreaUiState = bulkReviewTextAreaUiState,
                            bulkReviewMediaPickerUiState = bulkReviewMediaPickerUiState
                        )
                    }
                )
            }
            else -> emptyMap()
        }
    }

    private fun mapMiniActionsUiState(
        reviewForm: BulkReviewGetFormResponse.Data.ProductRevGetBulkForm.ReviewForm,
        bulkReviewTextAreaUiState: Map<String, BulkReviewTextAreaUiState>,
        bulkReviewMediaPickerUiState: Map<String, CreateReviewMediaPickerUiState>
    ): BulkReviewMiniActionsUiState {
        val inboxID = reviewForm.inboxID
        val miniActionTestimony = if (bulkReviewTextAreaUiState[inboxID] is BulkReviewTextAreaUiState.Hidden) {
            BulkReviewMiniActionUiState.Showing(
                iconUnifyId = IconUnify.EDIT,
                text = StringRes(R.string.tv_bulk_review_write_review_message)
            )
        } else {
            BulkReviewMiniActionUiState.Hidden(
                iconUnifyId = IconUnify.EDIT,
                text = StringRes(R.string.tv_bulk_review_write_review_message)
            )
        }
        val miniActionAttachment = if (bulkReviewMediaPickerUiState[inboxID] is CreateReviewMediaPickerUiState.Hidden) {
            BulkReviewMiniActionUiState.Showing(
                iconUnifyId = IconUnify.CAMERA,
                text = StringRes(R.string.tv_bulk_review_add_attachments)
            )
        } else {
            BulkReviewMiniActionUiState.Hidden(
                iconUnifyId = IconUnify.CAMERA,
                text = StringRes(R.string.tv_bulk_review_add_attachments)
            )
        }
        return if (
            miniActionTestimony is BulkReviewMiniActionUiState.Hidden &&
            miniActionAttachment is BulkReviewMiniActionUiState.Hidden
        ) {
            BulkReviewMiniActionsUiState.Hidden
        } else {
            BulkReviewMiniActionsUiState.Showing(
                inboxID = reviewForm.inboxID,
                miniActionTestimony = miniActionTestimony,
                miniActionAttachment = miniActionAttachment
            )
        }
    }
}
