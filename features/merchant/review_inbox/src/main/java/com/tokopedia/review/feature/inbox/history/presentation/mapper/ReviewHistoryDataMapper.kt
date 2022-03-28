package com.tokopedia.review.feature.inbox.history.presentation.mapper

import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewGalleryImage
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewGalleryVideo
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewMedia

object ReviewHistoryDataMapper {
    fun mapReviewHistoryDataToReviewMediaPreviewData(
        feedbackID: String,
        images: List<String>,
        videos: List<String>
    ): ProductrevGetReviewMedia {
        val mappedReviewMediaVideoData = videos.mapIndexed { index, video ->
            ReviewMedia(
                videoId = video,
                feedbackId = feedbackID,
                mediaNumber = index.plus(1)
            )
        }
        val mappedReviewMediaImageData = images.mapIndexed { index, image ->
            ReviewMedia(
                imageId = image,
                feedbackId = feedbackID,
                mediaNumber = index.plus(1).plus(mappedReviewMediaVideoData.size)
            )
        }
        val mappedReviewMediaData = mappedReviewMediaVideoData.plus(mappedReviewMediaImageData)
        val mappedReviewVideoData = videos.map {
            ReviewGalleryVideo(
                attachmentId = it,
                url = it,
                feedbackId = feedbackID
            )
        }
        val mappedReviewImageData = images.map {
            ReviewGalleryImage(
                attachmentId = it,
                thumbnailURL = it,
                fullsizeURL = it,
                description = "",
                feedbackId = feedbackID
            )
        }
        return ProductrevGetReviewMedia(
            reviewMedia = mappedReviewMediaData,
            detail = Detail(
                reviewDetail = emptyList(),
                reviewGalleryImages = mappedReviewImageData,
                reviewGalleryVideos = mappedReviewVideoData,
                mediaCountFmt = mappedReviewMediaData.size.toString(),
                mediaCount = mappedReviewMediaData.size.toLong()
            )
        )
    }
}