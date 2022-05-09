package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewMediaVideoThumbnailUiState : Parcelable {
    val attachmentID: String
    val reviewID: String
    val url: String

    @Parcelize
    data class Showing(
        val playable: Boolean = true,
        override val attachmentID: String = "",
        override val reviewID: String = "",
        override val url: String = ""
    ) : ReviewMediaVideoThumbnailUiState

    @Parcelize
    data class ShowingSeeMore(
        val playable: Boolean = false,
        val totalMediaCount: Int,
        val totalMediaCountFmt: String,
        override val attachmentID: String = "",
        override val reviewID: String = "",
        override val url: String = ""
    ) : ReviewMediaVideoThumbnailUiState
}