package com.tokopedia.review.feature.media.player.video.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewVideoPlaybackUiState: Parcelable {
    fun isPlaying(): Boolean {
        return this is Playing || this is Buffering
    }

    val currentPosition: Long

    @Parcelize
    data class Inactive(
        override val currentPosition: Long = 0L,
        val shouldPlayWhenActive: Boolean = false
    ) : ReviewVideoPlaybackUiState

    @Parcelize
    data class Playing(
        override val currentPosition: Long = 0L
    ) : ReviewVideoPlaybackUiState

    @Parcelize
    data class Buffering(
        override val currentPosition: Long = 0L
    ) : ReviewVideoPlaybackUiState

    @Parcelize
    data class Paused(
        override val currentPosition: Long = 0L
    ) : ReviewVideoPlaybackUiState

    @Parcelize
    data class Preloading(
        override val currentPosition: Long = 0L
    ) : ReviewVideoPlaybackUiState

    @Parcelize
    data class Ended(
        override val currentPosition: Long = 0L
    ) : ReviewVideoPlaybackUiState

    @Parcelize
    data class Error(
        override val currentPosition: Long = 0L,
        val errorCode: String = ""
    ) : ReviewVideoPlaybackUiState
}