package com.tokopedia.broadcaster

import android.content.Context
import android.os.Handler
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.tokopedia.broadcaster.lib.BroadcasterConnection
import com.tokopedia.broadcaster.listener.BroadcasterListener
import com.tokopedia.broadcaster.state.BroadcasterState
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView

interface LiveBroadcaster {
    val ingestUrl: String
    val state: BroadcasterState
    val config: BroadcasterConfig
    val connection: BroadcasterConnection

    fun init(context: Context, handler: Handler)
    fun prepare(config: BroadcasterConfig?)
    fun setListener(listener: BroadcasterListener)
    fun startPreview(surfaceView: SurfaceAspectRatioView)
    fun stopPreview()
    fun switchCamera()
    fun start(url: String)
    fun resume()
    fun pause()
    fun reconnect()
    fun stop()
    fun release()
}