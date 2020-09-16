package com.tokopedia.play.broadcaster.pusher.state


/**
 * Created by mzennis on 16/07/20.
 */
interface PlayPusherInfoListener {

    fun onStart() { }

    fun onStartPreviewing() { }

    fun onResume() { }

    fun onPause() { }

    fun onStop(timeElapsed: String) { }

    fun onTimerActive(remainingTime: String) { }

    fun onTimerAlmostFinish(minutesUntilFinished: Long) { }

    fun onTimerFinish() { }

    fun onReachMaximumPauseDuration() { }
}