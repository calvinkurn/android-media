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
    val description: DescriptionUiModel
)

data class MediaUiModel(
    val type: String,
    val url: String
)

data class LikeUiState(
    val count: Int,
    val state: LikeStatus
) {
    enum class LikeStatus(val value: Int) {
        Like(1),
        Dislike(2),
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
