package com.tokopedia.play_common.player.creator

import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.SimpleExoPlayer

/**
 * Created by jegul on 15/09/20
 */
interface ExoPlayerCreator {

    fun createExoPlayer(loadControl: LoadControl): SimpleExoPlayer
}