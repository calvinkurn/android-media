package com.tokopedia.review.feature.historydetails.presentation.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.common.data.ProductrevGetReviewDetail
import com.tokopedia.review.common.data.ReviewViewState
import com.tokopedia.review.common.data.Success
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState

object ReviewDetailDataMapper {

    private fun MutableList<ReviewMediaThumbnailVisitable>.includeImage(
        reviewDetailState: ReviewViewState<ProductrevGetReviewDetail>
    ) = apply {
        if (reviewDetailState is Success) {
            addAll(reviewDetailState.data.review.imageAttachments.map {
                ReviewMediaImageThumbnailUiModel(
                    uiState = ReviewMediaImageThumbnailUiState.Showing(
                        attachmentID = it.attachmentID,
                        reviewID = reviewDetailState.data.review.feedbackId,
                        thumbnailUrl = it.thumbnail,
                        fullSizeUrl = it.fullSize
                    )
                )
            })
        }
    }

    private fun MutableList<ReviewMediaThumbnailVisitable>.includeVideo(
        reviewDetailState: ReviewViewState<ProductrevGetReviewDetail>
    ) = apply {
        if (reviewDetailState is Success) {
            addAll(reviewDetailState.data.review.videoAttachments.map { video ->
                ReviewMediaVideoThumbnailUiModel(
                    uiState = ReviewMediaVideoThumbnailUiState.Showing(
                        attachmentID = video.attachmentID.orEmpty(),
                        reviewID = reviewDetailState.data.review.feedbackId,
                        url = video.url.orEmpty()
                    )
                )
            })
        }
    }

    fun mapReviewDetailDataToReviewMediaPreviewData(
        reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel?
    ): ProductrevGetReviewMedia {
        return ProductrevGetReviewMedia(
            reviewMedia = reviewMediaThumbnailUiModel?.generateReviewMedia().orEmpty(),
            detail = Detail(
                reviewDetail = emptyList(),
                reviewGalleryImages = reviewMediaThumbnailUiModel?.generateReviewGalleryImage().orEmpty(),
                reviewGalleryVideos = reviewMediaThumbnailUiModel?.generateReviewGalleryVideo().orEmpty(),
                mediaCountFmt = reviewMediaThumbnailUiModel?.generateMediaCount().orZero().toString(),
                mediaCount = reviewMediaThumbnailUiModel?.generateMediaCount().orZero()
            )
        )
    }

    fun mapReviewDetailToReviewMediaThumbnails(
        reviewDetailState: ReviewViewState<ProductrevGetReviewDetail>
    ): ReviewMediaThumbnailUiModel {
        val thumbnails = if (reviewDetailState is Success) {
            mutableListOf<ReviewMediaThumbnailVisitable>()
                .includeVideo(reviewDetailState)
                .includeImage(reviewDetailState)
        } else emptyList()
        return ReviewMediaThumbnailUiModel(thumbnails)
    }
}