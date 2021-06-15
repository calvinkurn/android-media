package com.tokopedia.play.broadcaster.pusher

import android.os.Handler
import android.view.SurfaceView


/**
 * Created by mzennis on 03/06/21.
 */
interface PlayLivePusher {

    val state: PlayLivePusherState

    fun init(handler: Handler)

    fun prepare(config: PlayLivePusherConfig? = null)

    fun setListener(listener: PlayLivePusherListener)

    fun startPreview(surfaceView: SurfaceView)

    fun stopPreview()

    fun switchCamera()

    fun start(url: String)

    fun resume()

    fun pause()

    fun reconnect()

    fun stop()
}