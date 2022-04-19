package com.tokopedia.play_common.player.state

import com.tokopedia.play_common.state.PlayVideoState

/**
 * Created by jegul on 27/08/20
 */
interface ExoPlayerStateProcessor {

    fun processState(playWhenReady: Boolean, playbackState: Int): PlayVideoState
}