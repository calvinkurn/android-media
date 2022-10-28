package com.tokopedia.review.feature.inbox.history.presentation.mapper

import com.tokopedia.review.common.data.ReviewViewState
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.feature.inbox.history.data.ProductrevFeedbackHistory
import com.tokopedia.review.feature.inbox.history.data.ProductrevFeedbackHistoryResponse
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState

object ReviewHistoryDataMapper {
    private fun MutableList<ReviewMediaThumbnailVisitable>.includeImage(
        reviewHistory: ProductrevFeedbackHistory
    ) = apply {
        addAll(reviewHistory.review.imageAttachments.map {
            ReviewMediaImageThumbnailUiModel(
                uiState = ReviewMediaImageThumbnailUiState.Showing(
                    attachmentID = it.attachmentID,
                    reviewID = reviewHistory.review.feedbackId,
                    thumbnailUrl = it.thumbnail,
                    fullSizeUrl = it.fullSize
                )
            )
        })
    }

    private fun MutableList<ReviewMediaThumbnailVisitable>.includeVideo(
        reviewHistory: ProductrevFeedbackHistory
    ) = apply {
        addAll(reviewHistory.review.videoAttachments.map { video ->
            ReviewMediaVideoThumbnailUiModel(
                uiState = ReviewMediaVideoThumbnailUiState.Showing(
                    attachmentID = video.attachmentID.orEmpty(),
                    reviewID = reviewHistory.review.feedbackId,
                    url = video.url.orEmpty()
                )
            )
        })
    }

    fun mapReviewHistoryDataToReviewMediaPreviewData(
        reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel
    ): ProductrevGetReviewMedia {
        return ProductrevGetReviewMedia(
            reviewMedia = reviewMediaThumbnailUiModel.generateReviewMedia(),
            detail = Detail(
                reviewDetail = emptyList(),
                reviewGalleryImages = reviewMediaThumbnailUiModel.generateReviewGalleryImage(),
                reviewGalleryVideos = reviewMediaThumbnailUiModel.generateReviewGalleryVideo(),
                mediaCountFmt = reviewMediaThumbnailUiModel.generateMediaCount().toString(),
                mediaCount = reviewMediaThumbnailUiModel.generateMediaCount()
            )
        )
    }

    fun mapReviewListToReviewHistoryList(
        reviewHistoryState: ReviewViewState<ProductrevFeedbackHistoryResponse>?
    ): List<ReviewHistoryUiModel> {
        return if (reviewHistoryState is Success) {
            reviewHistoryState.data.list.map { history ->
                ReviewHistoryUiModel(
                    history,
                    ReviewMediaThumbnailUiModel(
                        mutableListOf<ReviewMediaThumbnailVisitable>()
                            .includeVideo(history)
                            .includeImage(history)
                    )
                )
            }
        } else emptyList()
    }
}