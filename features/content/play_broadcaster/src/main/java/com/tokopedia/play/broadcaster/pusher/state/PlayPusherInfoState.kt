package com.tokopedia.play.broadcaster.pusher.state


/**
 * Created by mzennis on 27/05/20.
 */
sealed class PlayPusherInfoState {

    data class TimerActive(val timeRemaining: String) : PlayPusherInfoState()
    data class TimerAlmostFinish(val minutesUntilFinished: Long) : PlayPusherInfoState()
    data class TimerFinish(val timeElapsed: String) : PlayPusherInfoState()

    data class Error(val errorType: PlayPusherErrorType) : PlayPusherInfoState()
}