package com.tokopedia.play_common.player.creator

import android.content.Context
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.SimpleExoPlayer
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created by jegul on 15/09/20
 */
class DefaultExoPlayerCreator @Inject constructor(
        @ApplicationContext private val context: Context
) : ExoPlayerCreator {

    override fun createExoPlayer(loadControl: LoadControl): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(context)
                .setLoadControl(loadControl)
                .build()
    }
}