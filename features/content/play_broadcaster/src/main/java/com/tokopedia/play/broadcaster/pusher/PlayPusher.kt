package com.tokopedia.play.broadcaster.pusher

import android.view.SurfaceView


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

    fun resumePush()

    fun pausePush()

    fun destroy()

    /**
     * set live streaming duration
     */
    fun setStreamDuration(durationInMillis: Long)

    /**
     * set maximum live streaming duration
     */
    fun setMaxStreamDuration(durationInMillis: Long)

    /**
     * start live streaming timer
     */
    fun startTimer()

    /**
     * resume live streaming timer
     */
    fun resumeTimer()

    /**
     * pause live streaming timer
     */
    fun pauseTimer()

    /**
     * stop live streaming timer
     */
    fun stopTimer()

    /**
     * restart live streaming duration
     */
    fun restartStreamDuration(durationInMillis: Long)

    /**
     * set maximum pause duration when streaming
     */
    fun setMaxPauseDuration(durationInMillis: Long)

    fun getTimeElapsed(): String

    /**
     * set listener to timer
     */
    fun setPlayPusherTimerListener(listener: PlayPusherTimerListener)

    /**
     * set listener to pusher
     */
    fun setPlayPusherInfoListener(listener: PlayPusherInfoListener)
}