package com.tokopedia.play.broadcaster.pusher.timer


/**
 * Created by mzennis on 26/05/20.
 */
interface PlayPusherCountDownTimerListener {
    fun onCountDownActive(millisUntilFinished: Long)
    fun onCountDownFinish()
}