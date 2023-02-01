package com.tokopedia.review.feature.bulk_write_review.presentation.mapper

import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestParam
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewItemUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewProductInfoUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewMediaPickerUiState
import javax.inject.Inject

class BulkReviewSubmissionParamsMapper @Inject constructor() {
    fun map(
        bulkReviewItemUiModel: BulkReviewItemUiModel,
        anonymous: Boolean
    ): BulkReviewSubmitRequestParam? {
        val productInfoUiState = bulkReviewItemUiModel.uiState.productCardUiState
        val ratingUiState = bulkReviewItemUiModel.uiState.ratingUiState
        return if (productInfoUiState is BulkReviewProductInfoUiState.Showing && ratingUiState is BulkReviewRatingUiState.Showing) {
            BulkReviewSubmitRequestParam(
                inboxID = bulkReviewItemUiModel.inboxID,
                reputationID = bulkReviewItemUiModel.reputationID,
                productID = productInfoUiState.productID,
                shopID = bulkReviewItemUiModel.shopID,
                rating = ratingUiState.rating,
                reviewText = bulkReviewItemUiModel.getReviewItemTextAreaText(),
                isAnonymous = anonymous,
                attachmentIDs = bulkReviewItemUiModel.getReviewItemImageAttachmentIds(),
                utmSource = "",
                badRatingCategoryIDs = bulkReviewItemUiModel.getReviewItemSelectedBadRatingCategoryIds(),
                videoAttachments = bulkReviewItemUiModel.mapReviewItemVideoAttachment(),
                orderID = bulkReviewItemUiModel.orderID
            )
        } else {
            null
        }
    }

    private fun BulkReviewItemUiModel.mapReviewItemVideoAttachment(): List<BulkReviewSubmitRequestParam.VideoAttachment> {
        return uiState.mediaPickerUiState.let { mediaPickerUiState ->
            if (mediaPickerUiState is CreateReviewMediaPickerUiState.SuccessUpload) {
                mediaPickerUiState.mediaItems.filterIsInstance<CreateReviewMediaUiModel.Video>()
                    .map { mediaItem ->
                        BulkReviewSubmitRequestParam.VideoAttachment(
                            uploadID = mediaItem.uploadId,
                            url = mediaItem.remoteUrl
                        )
                    }
            } else {
                emptyList()
            }
        }
    }
}
