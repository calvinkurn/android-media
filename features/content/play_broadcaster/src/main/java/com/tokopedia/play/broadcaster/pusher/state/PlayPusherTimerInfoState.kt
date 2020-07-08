package com.tokopedia.play.broadcaster.pusher.state


/**
 * Created by mzennis on 27/05/20.
 */
sealed class PlayPusherTimerInfoState {
    data class TimerActive(val remainingTime: String) : PlayPusherTimerInfoState()
    data class TimerAlmostFinish(val minutesUntilFinished: Long) : PlayPusherTimerInfoState()
    data class TimerFinish(val timeElapsed: String) : PlayPusherTimerInfoState()
    object ReachMaximumPauseDuration : PlayPusherTimerInfoState()
}