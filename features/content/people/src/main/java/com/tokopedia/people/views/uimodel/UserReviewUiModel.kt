package com.tokopedia.people.views.uimodel

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
    )

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
