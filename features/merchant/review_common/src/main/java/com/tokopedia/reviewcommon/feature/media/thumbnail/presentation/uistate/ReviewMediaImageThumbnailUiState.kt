package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewMediaImageThumbnailUiState: Parcelable {
    val attachmentID: String
    val reviewID: String
    val thumbnailUrl: String
    val fullSizeUrl: String

    @Parcelize
    data class Showing(
        override val attachmentID: String = "",
        override val reviewID: String = "",
        override val thumbnailUrl: String = "",
        override val fullSizeUrl: String = "",
    ) : ReviewMediaImageThumbnailUiState

    @Parcelize
    data class ShowingSeeMore(
        override val attachmentID: String = "",
        override val reviewID: String = "",
        override val thumbnailUrl: String = "",
        override val fullSizeUrl: String = "",
        val totalMediaCount: Int,
        val totalMediaCountFmt: String,
    ) : ReviewMediaImageThumbnailUiState
}