package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 06/07/20.
 */
sealed class LivePusherTimerState {
    data class Active(val remainingTime: String) : LivePusherTimerState()
    data class AlmostFinish(val minutesLeft: Long) : LivePusherTimerState()
    object Finish : LivePusherTimerState()
}