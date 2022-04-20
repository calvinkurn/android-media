package com.tokopedia.review.feature.reading.presentation.mapper

import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewDetail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewGalleryImage
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewGalleryVideo
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewerUserInfo

object ReadReviewDataMapper {
    fun mapReadReviewDataToReviewMediaPreviewData(productReview: ProductReview, shopId: String): ProductrevGetReviewMedia {
        val mappedReviewMediaVideoData = productReview.videoAttachments.mapIndexed { index, videoAttachment ->
            ReviewMedia(
                videoId = videoAttachment.url,
                feedbackId = productReview.feedbackID,
                mediaNumber = index.plus(1)
            )
        }
        val mappedReviewMediaImageData = productReview.imageAttachments.mapIndexed { index, imageAttachment ->
            ReviewMedia(
                imageId = imageAttachment.uri,
                feedbackId = productReview.feedbackID,
                mediaNumber = index.plus(1).plus(mappedReviewMediaVideoData.size)
            )
        }
        val mappedReviewMediaData = mappedReviewMediaVideoData.plus(mappedReviewMediaImageData)
        val mappedReviewDetailData = ReviewDetail(
            shopId = shopId,
            user = ReviewerUserInfo(
                userId = productReview.user.userID,
                fullName = productReview.user.fullName,
                image = productReview.user.image,
                url = productReview.user.url
            ),
            feedbackId = productReview.feedbackID,
            variantName = productReview.variantName,
            rating = productReview.productRating,
            review = productReview.message,
            createTimestamp = productReview.reviewCreateTimestamp,
            isReportable = productReview.isReportable,
            isAnonymous = productReview.isAnonymous,
            isLiked = productReview.likeDislike.isLiked(),
            totalLike = productReview.likeDislike.totalLike,
            userStats = productReview.userReviewStats,
            badRatingReasonFmt = productReview.badRatingReasonFmt
        )
        val mappedReviewVideoData = productReview.videoAttachments.map {
            ReviewGalleryVideo(
                attachmentId = it.url,
                url = it.url,
                feedbackId = productReview.feedbackID,
            )
        }
        val mappedReviewImageData = productReview.imageAttachments.map {
            ReviewGalleryImage(
                attachmentId = it.uri,
                thumbnailURL = it.imageThumbnailUrl,
                fullsizeURL = it.uri,
                feedbackId = productReview.feedbackID,
            )
        }

        return ProductrevGetReviewMedia(
            reviewMedia = mappedReviewMediaData,
            detail = Detail(
                reviewDetail = listOf(mappedReviewDetailData),
                reviewGalleryImages = mappedReviewImageData,
                reviewGalleryVideos = mappedReviewVideoData,
                mediaCountFmt = mappedReviewMediaData.size.toString(),
                mediaCount = mappedReviewMediaData.size.toLong()
            )
        )
    }
}