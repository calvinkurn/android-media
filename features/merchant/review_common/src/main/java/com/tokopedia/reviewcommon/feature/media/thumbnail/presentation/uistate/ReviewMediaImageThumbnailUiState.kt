package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewMediaImageThumbnailUiState: Parcelable {
    val uri: String
    val removable: Boolean

    @Parcelize
    data class Showing(
        override val uri: String,
        override val removable: Boolean
    ) : ReviewMediaImageThumbnailUiState

    @Parcelize
    data class ShowingSeeMore(
        override val uri: String,
        override val removable: Boolean,
        val totalImageCount: Int
    ) : ReviewMediaImageThumbnailUiState

    @Parcelize
    data class Uploading(
        override val uri: String,
        override val removable: Boolean
    ) : ReviewMediaImageThumbnailUiState

    @Parcelize
    data class UploadFailed(
        override val uri: String,
        override val removable: Boolean
    ) : ReviewMediaImageThumbnailUiState
}