package com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewVideoPlayerUiState {
    val videoUri: String

    @Parcelize
    data class Initial(override val videoUri: String = "") : ReviewVideoPlayerUiState, Parcelable

    @Parcelize
    data class ChangingConfiguration(override val videoUri: String): ReviewVideoPlayerUiState, Parcelable

    @Parcelize
    data class RestoringState(
        override val videoUri: String = "",
        val playWhenReady: Boolean = false,
        val presentationTimeMs: Long = 0L
    ) : ReviewVideoPlayerUiState, Parcelable

    @Parcelize
    data class ReadyToPlay(override val videoUri: String = "") : ReviewVideoPlayerUiState, Parcelable
}