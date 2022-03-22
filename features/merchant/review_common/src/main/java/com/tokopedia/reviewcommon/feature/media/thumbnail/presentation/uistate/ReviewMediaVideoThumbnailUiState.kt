package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate

sealed interface ReviewMediaVideoThumbnailUiState {
    val uri: String
    val removable: Boolean
    val showDuration: Boolean

    data class Showing(
        val playable: Boolean,
        override val uri: String,
        override val removable: Boolean,
        override val showDuration: Boolean
    ) : ReviewMediaVideoThumbnailUiState

    data class Uploading(
        override val uri: String,
        override val removable: Boolean,
        override val showDuration: Boolean
    ) : ReviewMediaVideoThumbnailUiState

    data class UploadFailed(
        override val uri: String,
        override val removable: Boolean,
        override val showDuration: Boolean
    ) : ReviewMediaVideoThumbnailUiState
}