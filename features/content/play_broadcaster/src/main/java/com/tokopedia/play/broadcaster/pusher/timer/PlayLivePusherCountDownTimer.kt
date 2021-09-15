package com.tokopedia.play.broadcaster.pusher.timer


/**
 * Created by mzennis on 25/05/20.
 */
interface PlayLivePusherCountDownTimer {

    val remainingDurationInMillis: Long

    fun setDuration(duration: Long)

    fun setListener(listener: PlayLivePusherCountDownTimerListener)

    fun start()

    fun stop()

    fun restart(duration: Long)

    fun resume()

    fun pause()

    fun destroy()
}