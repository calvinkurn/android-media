package com.tokopedia.play.view.uimodel

import com.google.android.exoplayer2.ExoPlayer

/**
 * Created by jegul on 27/04/20
 */
sealed class VideoPlayerUiModel {

    val isYouTube: Boolean
        get() = this is YouTube

    val isGeneral: Boolean
        get() = this is General
}

data class General(
        val exoPlayer: ExoPlayer
) : VideoPlayerUiModel()

data class YouTube(
        val youtubeId: String
) : VideoPlayerUiModel()

object Unknown : VideoPlayerUiModel()