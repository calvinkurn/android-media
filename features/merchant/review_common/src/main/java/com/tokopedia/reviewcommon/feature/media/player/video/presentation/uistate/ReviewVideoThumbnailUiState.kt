package com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate

import android.graphics.Bitmap

sealed interface ReviewVideoThumbnailUiState {
    val videoThumbnail: Bitmap?

    data class Showed(
        override val videoThumbnail: Bitmap? = null
    ) : ReviewVideoThumbnailUiState

    data class Hidden(
        override val videoThumbnail: Bitmap? = null
    ) : ReviewVideoThumbnailUiState
}