package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 17/03/21.
 */
sealed class PlayTimerState {
    data class Active(val remainingTime: String) : PlayTimerState()
    data class AlmostFinish(val minutesLeft: Long) : PlayTimerState()
    object Finish : PlayTimerState()
}