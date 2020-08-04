package com.tokopedia.play.broadcaster.pusher.timer


/**
 * Created by mzennis on 26/05/20.
 */
interface PlayPusherTimerListener {

    /**
     * format => minutes:seconds
     */
    fun onCountDownActive(timeLeft: String)

    /**
     * when hitting the
     * 05:00 - 04:56
     * 02:00 - 01:56
     * minutes prior to end
     */
    fun onCountDownAlmostFinish(minutesUntilFinished: Long)

    fun onCountDownFinish()

    /**
     * when reach maximum pause duration
     */
    fun onReachMaximumPauseDuration()
}