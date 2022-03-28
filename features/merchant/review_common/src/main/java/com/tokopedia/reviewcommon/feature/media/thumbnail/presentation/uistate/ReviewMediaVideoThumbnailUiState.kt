package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewMediaVideoThumbnailUiState: Parcelable {
    val uri: String
    val removable: Boolean
    val showDuration: Boolean

    @Parcelize
    data class Showing(
        val playable: Boolean = false,
        override val uri: String = "",
        override val removable: Boolean = false,
        override val showDuration: Boolean = false
    ) : ReviewMediaVideoThumbnailUiState

    @Parcelize
    data class ShowingSeeMore(
        val playable: Boolean = false,
        val totalImageCount: Int,
        override val uri: String = "",
        override val removable: Boolean = false,
        override val showDuration: Boolean = false
    ) : ReviewMediaVideoThumbnailUiState

    @Parcelize
    data class Uploading(
        override val uri: String = "",
        override val removable: Boolean = false,
        override val showDuration: Boolean = false
    ) : ReviewMediaVideoThumbnailUiState

    @Parcelize
    data class UploadFailed(
        override val uri: String = "",
        override val removable: Boolean = false,
        override val showDuration: Boolean = false
    ) : ReviewMediaVideoThumbnailUiState
}