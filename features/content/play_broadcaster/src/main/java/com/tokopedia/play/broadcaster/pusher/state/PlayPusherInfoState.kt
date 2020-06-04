package com.tokopedia.play.broadcaster.pusher.state


/**
 * Created by mzennis on 27/05/20.
 */
sealed class PlayPusherInfoState {

    data class Active(val millisUntilFinished: Long, val duration: Long) : PlayPusherInfoState()
    object Finish : PlayPusherInfoState()
    object Error : PlayPusherInfoState()
}