package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import android.os.Handler
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView
import kotlin.jvm.Throws


/**
 * Created by mzennis on 03/06/21.
 */
interface PlayLivePusher {

    val state: PlayLivePusherState

    val config: PlayLivePusherConfig

    val ingestUrl: String

    @Throws(IllegalAccessException::class)
    fun init(context: Context, handler: Handler)

    fun prepare(config: PlayLivePusherConfig? = null)

    fun setListener(listener: PlayLivePusherListener)

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