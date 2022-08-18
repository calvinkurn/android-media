package com.tokopedia.review.feature.reading.presentation.mapper

import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewDetail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewerUserInfo
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

object ReadReviewDataMapper {
    fun mapReadReviewDataToReviewMediaPreviewData(
        productReview: ProductReview,
        reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel,
        shopId: String
    ): ProductrevGetReviewMedia {
        val mappedReviewDetailData = ReviewDetail(
            shopId = shopId,
            user = ReviewerUserInfo(
                userId = productReview.user.userID,
                fullName = productReview.user.fullName,
                image = productReview.user.image,
                url = productReview.user.url,
                label = productReview.user.label
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
        return ProductrevGetReviewMedia(
            reviewMedia = reviewMediaThumbnailUiModel.generateReviewMedia(),
            detail = Detail(
                reviewDetail = listOf(mappedReviewDetailData),
                reviewGalleryImages = reviewMediaThumbnailUiModel.generateReviewGalleryImage(),
                reviewGalleryVideos = reviewMediaThumbnailUiModel.generateReviewGalleryVideo(),
                mediaCountFmt = reviewMediaThumbnailUiModel.generateMediaCount().toString(),
                mediaCount = reviewMediaThumbnailUiModel.generateMediaCount()
            )
        )
    }
}