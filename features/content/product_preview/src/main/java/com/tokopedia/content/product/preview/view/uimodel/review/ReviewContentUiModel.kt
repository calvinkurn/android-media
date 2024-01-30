package com.tokopedia.content.product.preview.view.uimodel.review

import com.tokopedia.content.product.preview.view.uimodel.MediaType

/**
 * @author by astidhiyaa on 06/12/23
 */
data class ReviewContentUiModel(
    val reviewId: String,
    val medias: List<ReviewMediaUiModel>,
    val menus: ReviewMenuStatus,
    val likeState: ReviewLikeUiState,
    val author: ReviewAuthorUiModel,
    val description: ReviewDescriptionUiModel,
    val mediaSelectedPosition: Int,
) {
    companion object {
        val Empty
            get() = ReviewContentUiModel(
                reviewId = "",
                medias = emptyList(),
                menus = ReviewMenuStatus(isReportable = false),
                likeState = ReviewLikeUiState(0, ReviewLikeUiState.ReviewLikeStatus.Reset),
                author = ReviewAuthorUiModel("", "", "", "", ""),
                description = ReviewDescriptionUiModel(0, "", "", ""),
                mediaSelectedPosition = -1,
            )
    }
}

data class ReviewMediaUiModel(
    val mediaId: String = "",
    val type: MediaType = MediaType.Unknown,
    val url: String = "",
    val selected: Boolean = false,
)

data class ReviewLikeUiState(
    val count: Int,
    val state: ReviewLikeStatus,
    val withAnimation: Boolean = false // from double tap
) {
    enum class ReviewLikeStatus(val value: Int) {
        Like(1),
        Dislike(2), // Not yet
        Reset(3);

        companion object {
            private val values = values()

            fun getByValue(value: Int): ReviewLikeStatus {
                values.forEach {
                    if (it.value == value) return it
                }
                return Reset
            }

            val ReviewLikeStatus.switch: ReviewLikeStatus
                get() = if (this == Like) Reset else Like
        }
    }
}

data class ReviewAuthorUiModel(
    val name: String,
    val type: String,
    val id: String,
    val avatarUrl: String,
    val appLink: String
)

data class ReviewDescriptionUiModel(
    val stars: Int,
    val productType: String,
    val timestamp: String,
    val description: String
)

data class ReviewMenuStatus(
    val isReportable: Boolean
)
