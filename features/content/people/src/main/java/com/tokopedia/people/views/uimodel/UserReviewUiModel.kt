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
        fun mapToProductReviewMediaGalleryModel(userId: String): ProductrevGetReviewMedia {
            return ProductrevGetReviewMedia(
                reviewMedia = attachments.mapIndexed { index, attachment ->
                    val position = index + 1

                    when (attachment) {
                        is Attachment.Video -> {
                            ReviewMedia(
                                videoId = attachment.attachmentID,
                                feedbackId = feedbackID,
                                mediaNumber = position,
                            )
                        }
                        is Attachment.Image -> {
                            ReviewMedia(
                                imageId = attachment.attachmentID,
                                feedbackId = feedbackID,
                                mediaNumber = position,
                            )
                        }
                    }
                },
                detail = Detail(
                    reviewGalleryImages = attachments.filterIsInstance<Attachment.Image>().map {
                        ReviewGalleryImage(
                            attachmentId = it.attachmentID,
                            thumbnailURL = it.thumbnailUrl,
                            fullsizeURL = it.fullSizeUrl,
                            feedbackId = feedbackID,
                        )
                    },
                    reviewGalleryVideos = attachments.filterIsInstance<Attachment.Video>().map {
                        ReviewGalleryVideo(
                            attachmentId = it.attachmentID,
                            url = it.mediaUrl,
                            feedbackId = feedbackID,
                        )
                    },
                    mediaCount = attachments.size.toLong(),
                    reviewDetail = listOf(
                        ReviewDetail(
                            feedbackId = feedbackID,
                            variantName = product.productVariant.variantName,
                            review = reviewText,
                            rating = rating,
                            isLiked = likeDislike.isLike,
                            totalLike = likeDislike.totalLike,
                            user = ReviewerUserInfo(
                                userId = userId,
                            )
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

    sealed interface Attachment {

        data class Image(
            val attachmentID: String,
            val thumbnailUrl: String,
            val fullSizeUrl: String,
        ) : Attachment
        data class Video(
            val attachmentID: String,
            val mediaUrl: String,
        ) : Attachment
    }

    data class LikeDislike(
        val totalLike: Int,
        val isLike: Boolean
    )

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
