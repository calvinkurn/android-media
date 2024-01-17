package com.tokopedia.content.product.preview.view.uimodel

/**
 * @author by astidhiyaa on 06/12/23
 */
data class ReviewUiModel(
    val reviewId: String,
    val medias: List<MediaUiModel>,
    val menus: MenuStatus,
    val likeState: LikeUiState,
    val author: AuthorUiModel,
    val description: DescriptionUiModel,
) {
    companion object {
        val Empty
            get() = ReviewUiModel(
                reviewId = "",
                medias = emptyList(),
                menus = MenuStatus(isReportable = false),
                likeState = LikeUiState(0, LikeUiState.LikeStatus.Reset),
                author = AuthorUiModel("", "", "", "", ""),
                description = DescriptionUiModel(0, "", "", "")
            )
    }
}

data class MediaUiModel(
    val type: String,
    val url: String
)

data class LikeUiState(
    val count: Int,
    val state: LikeStatus,
    val withAnimation: Boolean = false, //from double tap
) {
    enum class LikeStatus(val value: Int) {
        Like(1),
        Dislike(2), //Not yet
        Reset(3);

        companion object {
            private val values = values()

            fun getByValue(value: Int): LikeStatus {
                values.forEach {
                    if (it.value == value) return it
                }
                return Reset
            }
        }
    }
}

data class AuthorUiModel(
    val name: String,
    val type: String,
    val id: String,
    val avatarUrl: String,
    val appLink: String
)

data class DescriptionUiModel(
    val stars: Int,
    val productType: String,
    val timestamp: String,
    val description: String
)

data class MenuStatus(
    val isReportable: Boolean
)

val LikeUiState.LikeStatus.switch: LikeUiState.LikeStatus
    get() = if (this == LikeUiState.LikeStatus.Like) LikeUiState.LikeStatus.Reset else LikeUiState.LikeStatus.Like

