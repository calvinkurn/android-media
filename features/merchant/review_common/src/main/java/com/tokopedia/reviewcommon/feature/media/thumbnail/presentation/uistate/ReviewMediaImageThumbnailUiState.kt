package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewMediaImageThumbnailUiState: Parcelable {
    val reviewID: String
    val thumbnailUrl: String
    val fullSizeUrl: String

    @Parcelize
    data class Showing(
        override val reviewID: String = "",
        override val thumbnailUrl: String = "",
        override val fullSizeUrl: String = "",
    ) : ReviewMediaImageThumbnailUiState

    @Parcelize
    data class ShowingSeeMore(
        override val reviewID: String = "",
        override val thumbnailUrl: String = "",
        override val fullSizeUrl: String = "",
        val totalImageCount: Int = 0
    ) : ReviewMediaImageThumbnailUiState
}