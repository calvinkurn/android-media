package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewMediaVideoThumbnailUiState: Parcelable {
    val uri: String

    @Parcelize
    data class Showing(
        val playable: Boolean = true,
        override val uri: String = ""
    ) : ReviewMediaVideoThumbnailUiState

    @Parcelize
    data class ShowingSeeMore(
        val playable: Boolean = false,
        val totalImageCount: Int,
        override val uri: String = ""
    ) : ReviewMediaVideoThumbnailUiState
}