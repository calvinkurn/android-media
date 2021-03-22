package com.tokopedia.play.view.uimodel.recom

import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.util.extension.exhaustive

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
                val exoPlayer: ExoPlayer
        ) : General()
    }

    object Unknown : PlayVideoPlayerUiModel()
}

data class PlayGeneralVideoPlayerParams(
        val videoUrl: String,
        val buffer: PlayBufferControl,
        val lastMillis: Long?
)

fun PlayVideoPlayerUiModel.General.Incomplete.setPlayer(videoPlayer: ExoPlayer) = PlayVideoPlayerUiModel.General.Complete(
        params = params,
        exoPlayer = videoPlayer,
)

val PlayVideoPlayerUiModel.isYouTube: Boolean
    get() = this is PlayVideoPlayerUiModel.YouTube

val PlayVideoPlayerUiModel.isGeneral: Boolean
    get() = this is PlayVideoPlayerUiModel.General

fun PlayVideoPlayerUiModel.General.updateParams(
        newParams: PlayGeneralVideoPlayerParams
): PlayVideoPlayerUiModel.General {
    return when(this) {
        is PlayVideoPlayerUiModel.General.Incomplete -> copy(params = newParams)
        is PlayVideoPlayerUiModel.General.Complete -> copy(params = newParams)
    }.exhaustive
}