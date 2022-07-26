package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel

import android.os.Parcelable
import com.tokopedia.reviewcommon.extension.isMoreThanZero
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewGalleryImage
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewGalleryVideo
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewMedia
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewMediaThumbnailUiModel(
    val mediaThumbnails: List<ReviewMediaThumbnailVisitable> = emptyList()
) : Parcelable {

    private fun MutableList<ReviewMedia>.includeVideo() = apply {
        addAll(mediaThumbnails.filterIsInstance<ReviewMediaVideoThumbnailUiModel>()
            .mapIndexed { index, video ->
                ReviewMedia(
                    videoId = video.getAttachmentID(),
                    feedbackId = video.getReviewID(),
                    mediaNumber = index.plus(1)
                )
            })
    }

    private fun MutableList<ReviewMedia>.includeImage() = apply {
        addAll(mediaThumbnails.filterIsInstance<ReviewMediaImageThumbnailUiModel>()
            .mapIndexed { index, image ->
                ReviewMedia(
                    imageId = image.getAttachmentID(),
                    feedbackId = image.getReviewID(),
                    mediaNumber = index.plus(1).plus(size)
                )
            })
    }

    fun isShowingSeeMore() = mediaThumbnails.any { it.isShowingSeeMore() }

    fun generateReviewMedia() = mutableListOf<ReviewMedia>().includeVideo().includeImage()

    fun generateReviewGalleryImage() =
        mediaThumbnails.filterIsInstance<ReviewMediaImageThumbnailUiModel>().map { image ->
            ReviewGalleryImage(
                attachmentId = image.getAttachmentID(),
                thumbnailURL = image.uiState.thumbnailUrl,
                fullsizeURL = image.uiState.fullSizeUrl,
                feedbackId = image.getReviewID()
            )
        }

    fun generateReviewGalleryVideo() =
        mediaThumbnails.filterIsInstance<ReviewMediaVideoThumbnailUiModel>().map { video ->
            ReviewGalleryVideo(
                attachmentId = video.getAttachmentID(),
                url = video.uiState.url,
                feedbackId = video.getReviewID()
            )
        }

    fun generateMediaCount() = mediaThumbnails.lastOrNull()?.getTotalMediaCount()?.takeIf {
        it.isMoreThanZero()
    } ?: mediaThumbnails.size.toLong()
}