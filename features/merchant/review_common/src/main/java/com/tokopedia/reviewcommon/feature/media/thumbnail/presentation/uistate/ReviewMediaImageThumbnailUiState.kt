package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate

sealed interface ReviewMediaImageThumbnailUiState {
    val uri: String
    val removable: Boolean

    data class Showing(
        override val uri: String,
        override val removable: Boolean
    ) : ReviewMediaImageThumbnailUiState

    data class Uploading(
        override val uri: String,
        override val removable: Boolean
    ) : ReviewMediaImageThumbnailUiState

    data class UploadFailed(
        override val uri: String,
        override val removable: Boolean
    ) : ReviewMediaImageThumbnailUiState
}