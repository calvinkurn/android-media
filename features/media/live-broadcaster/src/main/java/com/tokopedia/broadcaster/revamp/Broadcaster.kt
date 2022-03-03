package com.tokopedia.broadcaster.revamp

import android.content.Context
import android.os.Handler
import android.view.SurfaceHolder
import com.tokopedia.broadcaster.revamp.util.log.BroadcasterLogger

/**
 * Created by meyta.taliti on 01/03/22.
 */
interface Broadcaster {

    fun setCallback(callback: Callback?)

    fun setLogger(logger: BroadcasterLogger)

    fun create(holder: SurfaceHolder, surfaceSize: Size)

    fun updateSurfaceSize(surfaceSize: Size)

    fun start(rtmpUrl: String)

    fun stop()

    fun release()

    fun flip()

    fun snapShot()

    interface Callback {

        fun getHandler(): Handler?

        fun getActivityContext(): Context

        fun updateAspectFrameSize(size: Size)
    }

    data class Size(
        val width: Int,
        val height: Int
    )
}