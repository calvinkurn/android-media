package com.tokopedia.play.view.uimodel


/**
 * Created by mzennis on 2020-01-08.
 */
data class TotalLikeUiModel(
        val totalLike: Int,
        val totalLikeFormatted: String
) {

    companion object {
        fun empty() = TotalLikeUiModel(0, "0")
    }
}