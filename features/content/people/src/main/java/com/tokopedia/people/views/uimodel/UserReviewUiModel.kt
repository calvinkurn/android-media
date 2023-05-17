package com.tokopedia.people.views.uimodel

import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewDetail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewGalleryImage
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewGalleryVideo
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewerUserInfo

/**
 * Created By : Jonathan Darwin on May 12, 2023
 */
data class UserReviewUiModel(
    val reviewList: List<Review>,
    val page: Int,
    val hasNext: Boolean,
    val status: Status
) {
    val isLoading: Boolean
        get() = status == Status.Loading

    data class Review(
        val feedbackID: String,
        val product: Product,
        val rating: Int,
        val reviewText: String,
        val reviewTime: String,
        val attachments: List<Attachment>,
        val likeDislike: LikeDislike,
        val isReviewTextExpanded: Boolean,
    ) {
        fun mapToProductReviewMediaGalleryModel(
            userId: String,
            userDisplayName: String,
            userImage: String,
        ): ProductrevGetReviewMedia {
            return ProductrevGetReviewMedia(
                reviewMedia = attachments.mapIndexed { index, attachment ->
                    val position = index + 1

                    if (attachment.isVideo) {
                        ReviewMedia(
                            videoId = attachment.attachmentID,
                            feedbackId = feedbackID,
                            mediaNumber = position,
                        )
                    } else {
                        ReviewMedia(
                            imageId = attachment.attachmentID,
                            feedbackId = feedbackID,
                            mediaNumber = position,
                        )
                    }
                },
                detail = Detail(
                    reviewGalleryImages = attachments.filter { !it.isVideo }.map {
                        ReviewGalleryImage(
                            attachmentId = it.attachmentID,
                            thumbnailURL = it.mediaUrl,
                            fullsizeURL = it.mediaUrl,
                            feedbackId = feedbackID,
                        )
                    },
                    reviewGalleryVideos = attachments.filter { it.isVideo }.map {
                        ReviewGalleryVideo(
                            attachmentId = it.attachmentID,
                            url = it.mediaUrl,
                            feedbackId = feedbackID,
                        )
                    },
                    mediaCount = attachments.size.toLong(),
                    reviewDetail = listOf(
                        ReviewDetail(
                            user = ReviewerUserInfo(
                                userId = userId,
                                fullName = userDisplayName,
                                image = userImage,
                            ),
                            feedbackId = feedbackID,
                            variantName = product.productVariant.variantName,
                            review = reviewText,
                            rating = rating,
                            isLiked = likeDislike.isLike,
                            totalLike = likeDislike.totalLike,
                        )
                    )
                )
            )
        }
    }

    data class Product(
        val productID: String,
        val productName: String,
        val productImageURL: String,
        val productPageURL: String,
        val productStatus: Int,
        val productVariant: ProductVariant
    )

    data class ProductVariant(
        val variantID: String,
        val variantName: String
    )

    data class Attachment(
        val attachmentID: String,
        val mediaUrl: String,
        val type: Type,
    ) {
        val isVideo: Boolean
            get() = type == Type.Video

        enum class Type {
            Video, Image,
        }
    }

    data class LikeDislike(
        val totalLike: Int,
        val likeStatus: Int
    ) {
        fun switchLikeStatus(): Int {
            return if (likeStatus == LIKE_STATUS) RESET_STATUS else LIKE_STATUS
        }

        val isLike: Boolean
            get() = likeStatus == LIKE_STATUS

        companion object {
            private const val LIKE_STATUS = 1
            private const val RESET_STATUS = 3
        }
    }

    companion object {
        val Empty: UserReviewUiModel
            get() = UserReviewUiModel(
                reviewList = emptyList(),
                page = 1,
                hasNext = true,
                status = Status.Unknown
            )
    }

    enum class Status {
        Unknown, Loading, Success, Error
    }
}
