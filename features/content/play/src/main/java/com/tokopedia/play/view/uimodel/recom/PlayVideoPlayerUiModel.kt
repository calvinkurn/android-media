package com.tokopedia.play.view.uimodel.recom

import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play_common.model.PlayBufferControl

/**
 * Created by jegul on 08/02/21
 */
sealed class PlayVideoPlayerUiModel {

    data class YouTube(
            val youtubeId: String
    ) : PlayVideoPlayerUiModel()

    sealed class General : PlayVideoPlayerUiModel() {

        abstract val params: PlayGeneralVideoPlayerParams

        data class Incomplete(override val params: PlayGeneralVideoPlayerParams) : General()
        data class Complete(
                override val params: PlayGeneralVideoPlayerParams,
                val exoPlayer: ExoPlayer,
                val lastMillis: Long?
        ) : General()
    }

    object Unknown : PlayVideoPlayerUiModel()
}

data class PlayGeneralVideoPlayerParams(
        val videoUrl: String,
        val buffer: PlayBufferControl
)

fun PlayVideoPlayerUiModel.General.Incomplete.setPlayer(videoPlayer: ExoPlayer) = PlayVideoPlayerUiModel.General.Complete(
        params = params,
        exoPlayer = videoPlayer,
        lastMillis = null
)

val PlayVideoPlayerUiModel.isYouTube: Boolean
    get() = this is PlayVideoPlayerUiModel.YouTube

val PlayVideoPlayerUiModel.isGeneral: Boolean
    get() = this is PlayVideoPlayerUiModel.General