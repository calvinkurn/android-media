package com.tokopedia.play.view.uimodel.recom

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.util.extension.exhaustive
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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
                val exoPlayer: Player,
                val playerType: PlayerType
        ) : General()
    }

    object Unknown : PlayVideoPlayerUiModel()
}

enum class PlayerType {

    Client,
    Cast
}

data class PlayGeneralVideoPlayerParams(
        val videoUrl: String,
        val buffer: PlayBufferControl,
        val lastMillis: Long?
)

fun PlayVideoPlayerUiModel.General.setPlayer(videoPlayer: Player) = PlayVideoPlayerUiModel.General.Complete(
        params = params,
        exoPlayer = videoPlayer,
        playerType = if (videoPlayer is ExoPlayer) PlayerType.Client else PlayerType.Cast
)

val PlayVideoPlayerUiModel.isYouTube: Boolean
    get() = this is PlayVideoPlayerUiModel.YouTube

@OptIn(ExperimentalContracts::class)
fun PlayVideoPlayerUiModel.isGeneral(): Boolean {
    contract {
        returns(true) implies (this@isGeneral is PlayVideoPlayerUiModel.General)
    }
    return this is PlayVideoPlayerUiModel.General
}

fun PlayVideoPlayerUiModel.General.updateParams(
        newParams: PlayGeneralVideoPlayerParams
): PlayVideoPlayerUiModel.General {
    return when(this) {
        is PlayVideoPlayerUiModel.General.Incomplete -> copy(params = newParams)
        is PlayVideoPlayerUiModel.General.Complete -> copy(params = newParams)
    }.exhaustive
}