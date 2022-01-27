package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 17/03/21.
 */
sealed class PlayLiveTimerState {
    data class Active(val remainingInMs: Long) : PlayLiveTimerState()
    object Finish : PlayLiveTimerState()
}