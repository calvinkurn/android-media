package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewMediaImageThumbnailUiState: Parcelable {
    val uri: String

    @Parcelize
    data class Showing(
        override val uri: String = ""
    ) : ReviewMediaImageThumbnailUiState

    @Parcelize
    data class ShowingSeeMore(
        override val uri: String = "",
        val totalImageCount: Int = 0
    ) : ReviewMediaImageThumbnailUiState
}