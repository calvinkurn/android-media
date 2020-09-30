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
     * add live streaming duration
     */
    fun addStreamDuration(durationInMillis: Long)

    /**
     * add maximum live streaming duration
     */
    fun addMaxStreamDuration(durationInMillis: Long)

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
     * add maximum pause duration when streaming
     */
    fun addMaxPauseDuration(durationInMillis: Long)

    fun getTimeElapsed(): String

    /**
     * add listener to timer
     */
    fun addPlayPusherTimerListener(listener: PlayPusherTimerListener)

    /**
     * add listener to pusher
     */
    fun addPlayPusherInfoListener(listener: PlayPusherInfoListener)
}