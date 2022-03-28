package com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewVideoPlaybackUiState {
    val currentPosition: Long

    @Parcelize
    data class Inactive(
        override val currentPosition: Long = 0L,
        val shouldPlayWhenActive: Boolean = false
    ) : ReviewVideoPlaybackUiState, Parcelable

    @Parcelize
    data class Playing(
        override val currentPosition: Long = 0L
    ) : ReviewVideoPlaybackUiState, Parcelable

    @Parcelize
    data class Buffering(
        override val currentPosition: Long = 0L
    ) : ReviewVideoPlaybackUiState, Parcelable

    @Parcelize
    data class Paused(
        override val currentPosition: Long = 0L
    ) : ReviewVideoPlaybackUiState, Parcelable

    @Parcelize
    data class Preloading(
        override val currentPosition: Long = 0L
    ) : ReviewVideoPlaybackUiState, Parcelable

    @Parcelize
    data class Ended(
        override val currentPosition: Long = 0L
    ) : ReviewVideoPlaybackUiState, Parcelable
}