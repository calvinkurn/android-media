package com.tokopedia.play.view.type

import com.google.android.exoplayer2.ExoPlayer

/**
 * Created by jegul on 05/12/19
 */
sealed class PlayVODType {

    abstract val exoPlayer: ExoPlayer

    data class Live(override val exoPlayer: ExoPlayer) : PlayVODType()
    data class Replay(override val exoPlayer: ExoPlayer) : PlayVODType()
}