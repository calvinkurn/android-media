package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 06/07/20.
 */
sealed class BroadcastTimerState {
    data class Active(val remainingTime: String) : BroadcastTimerState()
    data class AlmostFinish(val minutesLeft: Long) : BroadcastTimerState()
    object Finish : BroadcastTimerState()
    object ReachMaximumPauseDuration : BroadcastTimerState()
}