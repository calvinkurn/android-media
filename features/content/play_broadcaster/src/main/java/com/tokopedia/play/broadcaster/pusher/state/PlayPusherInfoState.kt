package com.tokopedia.play.broadcaster.pusher.state


/**
 * Created by mzennis on 27/05/20.
 */
sealed class PlayPusherInfoState {

    data class Active(val timeLeft: String) : PlayPusherInfoState()

    data class AlmostFinish(val minutesUntilFinished: Long) : PlayPusherInfoState()

    data class Finish(val timeElapsed: String) : PlayPusherInfoState()

    data class Error(val errorType: PlayPusherErrorType) : PlayPusherInfoState()
}