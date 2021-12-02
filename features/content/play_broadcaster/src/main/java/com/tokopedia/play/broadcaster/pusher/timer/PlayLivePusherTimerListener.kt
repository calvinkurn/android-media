package com.tokopedia.play.broadcaster.pusher.timer


/**
 * Created by mzennis on 03/08/21.
 */
interface PlayLivePusherTimerListener {

    fun onCountDownTimerActive(timeInMillis: Long)

    fun onCountDownTimerFinish()
}