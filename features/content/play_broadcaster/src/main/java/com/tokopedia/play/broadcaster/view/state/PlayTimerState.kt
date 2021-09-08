package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 17/03/21.
 */
sealed class PlayTimerState {
    data class Active(val remainingInMs: Long) : PlayTimerState()
    data class AlmostFinish(val remainingInMinutes: Long) : PlayTimerState()
    object Finish : PlayTimerState()
}