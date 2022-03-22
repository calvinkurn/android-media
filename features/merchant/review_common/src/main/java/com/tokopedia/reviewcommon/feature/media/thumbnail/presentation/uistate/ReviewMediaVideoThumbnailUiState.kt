package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewMediaVideoThumbnailUiState: Parcelable {
    val uri: String
    val removable: Boolean
    val showDuration: Boolean

    @Parcelize
    data class Showing(
        val playable: Boolean,
        override val uri: String,
        override val removable: Boolean,
        override val showDuration: Boolean
    ) : ReviewMediaVideoThumbnailUiState

    @Parcelize
    data class ShowingSeeMore(
        val playable: Boolean,
        val totalImageCount: Int,
        override val uri: String,
        override val removable: Boolean,
        override val showDuration: Boolean
    ) : ReviewMediaVideoThumbnailUiState

    @Parcelize
    data class Uploading(
        override val uri: String,
        override val removable: Boolean,
        override val showDuration: Boolean
    ) : ReviewMediaVideoThumbnailUiState

    @Parcelize
    data class UploadFailed(
        override val uri: String,
        override val removable: Boolean,
        override val showDuration: Boolean
    ) : ReviewMediaVideoThumbnailUiState
}