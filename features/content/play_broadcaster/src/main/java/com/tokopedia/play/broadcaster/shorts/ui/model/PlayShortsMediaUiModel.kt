package com.tokopedia.play.broadcaster.shorts.ui.model

import com.google.android.exoplayer2.ExoPlayer

/**
 * Created By : Jonathan Darwin on November 11, 2022
 */
data class PlayShortsMediaUiModel(
    val mediaUri: String,
    val exoPlayer: ExoPlayer,
) {
    companion object {
        fun create(exoPlayer: ExoPlayer): PlayShortsMediaUiModel {
            return PlayShortsMediaUiModel(
                mediaUri = "",
                exoPlayer = exoPlayer,
            )
        }
    }
}
