package com.tokopedia.people.builder

import com.tokopedia.people.views.uimodel.UserReviewUiModel

/**
 * Created By : Jonathan Darwin on June 15, 2023
 */
class UserReviewModelBuilder {

    fun buildReviewList(
        size: Int = 5,
        page: Int = 1,
        hasNext: Boolean = true,
        status: UserReviewUiModel.Status = UserReviewUiModel.Status.Success,
    ): UserReviewUiModel {
        return UserReviewUiModel(
            reviewList = List(size) {
                UserReviewUiModel.Review(
                    feedbackID = it.toString(),
                    product = UserReviewUiModel.Product(
                        productID = it.toString(),
                        productName = "Product $it",
                        productImageURL = "",
                        productPageURL = "",
                        productStatus = 0,
                        productVariant = UserReviewUiModel.ProductVariant(
                            variantID = it.toString(),
                            variantName = "Variant $it"
                        )
                    ),
                    rating = it.coerceAtMost(5),
                    reviewText = List(25) { "review $it " }.joinToString(),
                    reviewTime = "1 bulan lalu",
                    attachments = listOf(
                        UserReviewUiModel.Attachment.Image(
                            attachmentID = it.toString(),
                            thumbnailUrl = "",
                            fullSizeUrl = "",
                        ),
                        UserReviewUiModel.Attachment.Video(
                            attachmentID = (it + 1).toString(),
                            mediaUrl = "",
                        )
                    ),
                    likeDislike = UserReviewUiModel.LikeDislike(
                        totalLike = 123,
                        isLike = true,
                    ),
                    isReviewTextExpanded = false,
                )
            },
            page = page,
            hasNext = hasNext,
            status = status,
        )
    }

    fun buildLikeDislike(
        totalLike: Int = 123,
        isLike: Boolean = true,
    ): UserReviewUiModel.LikeDislike {
        return UserReviewUiModel.LikeDislike(
            totalLike = totalLike,
            isLike = isLike,
        )
    }
}
