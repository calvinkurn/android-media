package com.tokopedia.play.broadcaster.pusher.apsara

import android.view.SurfaceView
import com.tokopedia.play.broadcaster.pusher.timer.PlayPusherTimerListener


/**
 * Created by mzennis on 22/09/20.
 */
interface PlayPusher {

    fun init()

    fun startPreview(surfaceView: SurfaceView)

    fun stopPreview()

    fun startPush(ingestUrl: String)

    fun restartPush()

    fun stopPush()

    fun switchCamera()

    fun resume()

    fun pause()

    fun destroy()

    /**
     * add live streaming duration
     */
    fun addStreamDuration(durationInMillis: Long)

    /**
     * add maximum live streaming duration
     */
    fun addMaxStreamDuration(durationInMillis: Long)

    /**
     * restart live streaming duration
     */
    fun restartStreamDuration(durationInMillis: Long)

    /**
     * add maximum pause duration when streaming
     */
    fun addMaxPauseDuration(durationInMillis: Long)

    /**
     * add listener to timer
     */
    fun addPlayPusherTimerListener(listener: PlayPusherTimerListener)

    /**
     * add listener to pusher
     */
    fun addPlayPusherInfoListener(listener: ApsaraLivePusherInfoListener)
}