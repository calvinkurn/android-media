package com.tokopedia.play.broadcaster.pusher.timer


/**
 * Created by mzennis on 26/05/20.
 */
interface PlayPusherTimerListener {
    fun onCountDownActive(
            elapsedTime: String,
            isFiveMinutesLeft: Boolean,
            isTwoMinutesLeft: Boolean
    )
    fun onCountDownFinish()
}