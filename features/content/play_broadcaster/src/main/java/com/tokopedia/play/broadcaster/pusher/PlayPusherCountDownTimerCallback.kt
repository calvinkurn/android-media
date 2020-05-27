package com.tokopedia.play.broadcaster.pusher


/**
 * Created by mzennis on 26/05/20.
 */
interface PlayPusherCountDownTimerCallback {
    fun onCountDownActive(millisUntilFinished: Long)
    fun onCountDownFinish()
}