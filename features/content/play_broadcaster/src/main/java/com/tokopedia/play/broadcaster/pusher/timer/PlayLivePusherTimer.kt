package com.tokopedia.play.broadcaster.pusher.timer


/**
 * Created by mzennis on 25/05/20.
 */
interface PlayLivePusherTimer {

    val remainingDurationInMillis: Long

    fun setDuration(duration: Long, maxDuration: Long)

    fun setListener(listener: PlayLivePusherTimerListener)

    fun start()

    fun stop()

    fun restart(duration: Long, maxDuration: Long)

    fun resume()

    fun pause()

    fun destroy()
}