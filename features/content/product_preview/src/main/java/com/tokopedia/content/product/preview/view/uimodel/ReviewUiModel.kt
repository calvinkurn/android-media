package com.tokopedia.content.product.preview.view.uimodel

import com.tokopedia.content.common.report_content.model.ContentMenuItem

/**
 * @author by astidhiyaa on 06/12/23
 */
data class ReviewUiModel(
    val id: String,
    val medias: List<MediaUiModel>,
    val menus: List<ContentMenuItem>,
    val likeState: LikeUiState,
    val author: AuthorUiModel,
    val description: DescriptionUiModel,
) {
    data class MediaUiModel(
        val type: String,
        val url: String,
    ) {
        enum class MediaType(val value: String) {
            Image(""), Video("") //check response
        }
    }

    data class LikeUiState(
        val count: Int,
        val state: LikeStatus
    ) {
        enum class LikeStatus(val value: Int) {
            Like(1),
            Dislike(2),
            Reset(3);
        }
    }

    data class AuthorUiModel(
        val name: String,
        val type: ReviewerType,
    ) {
        enum class ReviewerType(val value: String) { //TODO need to adjust with backend
            Complete("total-complete-review"),
            Juara("");
        }
    }

    data class DescriptionUiModel(
        val stars: Int,
        val productType: String,
        val timestamp: String,
        val description: String,
    )
}
