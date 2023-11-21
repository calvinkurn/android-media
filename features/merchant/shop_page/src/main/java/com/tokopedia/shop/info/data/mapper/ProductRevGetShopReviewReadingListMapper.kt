package com.tokopedia.shop.info.data.mapper

import com.tokopedia.shop.info.data.response.ProductRevGetShopReviewReadingListResponse
import com.tokopedia.shop.info.domain.entity.ShopReview
import javax.inject.Inject

class ProductRevGetShopReviewReadingListMapper @Inject constructor() {
    fun map(response: ProductRevGetShopReviewReadingListResponse): ShopReview {
        return ShopReview(
            totalReviews = response.productrevGetShopReviewReadingList.totalReviews,
            reviews = response.productrevGetShopReviewReadingList.list.map { review ->
                ShopReview.Review(
                    reviewId = review.reviewID,
                    rating = review.rating,
                    reviewTime = review.reviewTime,
                    reviewText = review.reviewText,
                    reviewerId = review.reviewerID,
                    reviewerName = review.reviewerName,
                    reviewerLabel = review.reviewerLabel,
                    likeDislike = ShopReview.Review.LikeDislike(
                        totalLike = review.likeDislike.totalLike,
                        likeStatus = review.likeDislike.likeStatus
                    ),
                    avatar = review.avatar,
                    attachments = review.toAttachments(),
                    product = ShopReview.Review.Product(
                        productId = review.product.productID,
                        productName = review.product.productName,
                        productVariant = ShopReview.Review.Product.ProductVariant(
                            variantId = review.product.productVariant.variantId,
                            variantName = review.product.productVariant.variantName
                        )
                    ),
                    badRatingReasonFmt = review.badRatingReasonFmt,
                    state = ShopReview.Review.State(
                        isReportable = review.state.isReportable,
                        isAutoReply = review.state.isAutoReply,
                        isAnonymous = review.state.isAnonymous
                    )
                )
            }
        )
    }

    private fun ProductRevGetShopReviewReadingListResponse.ProductRevGetShopReviewReadingList.ShopReviewList.toAttachments(): List<ShopReview.Review.Attachment> {
        return attachments.map { attachment ->
            ShopReview.Review.Attachment(
                attachment.attachmentId,
                attachment.thumbnailURL,
                attachment.fullSizeURL
            )
        }
    }
}
