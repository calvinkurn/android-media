package com.tokopedia.play_common.model

import com.google.android.exoplayer2.SimpleExoPlayer
import com.tokopedia.play_common.player.PlayVideoLoadControl

/**
 * Created by jegul on 12/03/20
 */
data class PlayPlayerModel(
        val player: SimpleExoPlayer,
        val loadControl: PlayVideoLoadControl
)