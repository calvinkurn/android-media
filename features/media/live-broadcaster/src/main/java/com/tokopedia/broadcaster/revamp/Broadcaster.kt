package com.tokopedia.broadcaster.revamp

import android.content.Context
import android.os.Handler
import android.view.SurfaceHolder
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterStatistic
import com.wmspanel.libstream.Streamer

/**
 * Created by meyta.taliti on 01/03/22.
 */
interface Broadcaster {

    fun setListener(listener: Listener)

    fun create(holder: SurfaceHolder, surfaceSize: Streamer.Size)

    fun start(rtmpUrl: String)

    fun stop()

    fun release()

    fun flip()

    fun snapShot()

    interface Listener {

        val handler: Handler

        val context: Context?

        fun updatePreviewRatio(size: Streamer.Size)

        fun onStatisticInfoChanged(statistic: BroadcasterStatistic)
    }
}