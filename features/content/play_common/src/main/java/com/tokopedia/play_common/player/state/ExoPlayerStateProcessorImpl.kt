package com.tokopedia.play_common.player.state

import com.google.android.exoplayer2.Player
import com.tokopedia.play_common.state.PlayVideoState

/**
 * Created by jegul on 27/08/20
 */
class ExoPlayerStateProcessorImpl : ExoPlayerStateProcessor {

    override fun processState(playWhenReady: Boolean, playbackState: Int): PlayVideoState {
        return when (playbackState) {
            Player.STATE_IDLE -> PlayVideoState.NoMedia
            Player.STATE_BUFFERING -> PlayVideoState.Buffering
            Player.STATE_READY -> if (!playWhenReady) PlayVideoState.Pause else PlayVideoState.Playing
            Player.STATE_ENDED -> PlayVideoState.Ended
            else -> throw IllegalStateException("State $playbackState is not defined")
        }
    }
}