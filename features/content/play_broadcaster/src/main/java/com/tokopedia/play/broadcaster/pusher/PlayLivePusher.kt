package com.tokopedia.play.broadcaster.pusher

import com.pedro.rtplibrary.view.LightOpenGlView


/**
 * Created by mzennis on 03/06/21.
 */
interface PlayLivePusher {

    val state: PlayLivePusherState

    fun prepare(config: PlayLivePusherConfig? = null)

    fun setListener(listener: PlayLivePusherListener)

    fun startPreview(lightOpenGlView: LightOpenGlView)

    fun stopPreview()

    fun switchCamera()

    fun start(url: String)

    fun resume()

    fun pause()

    fun reconnect(reason: String)

    fun stop()
}