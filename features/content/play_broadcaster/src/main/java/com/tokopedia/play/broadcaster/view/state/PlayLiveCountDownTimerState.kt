package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 17/03/21.
 */
sealed class PlayLiveCountDownTimerState {
    data class Active(val remainingInMs: Long) : PlayLiveCountDownTimerState()
    object Finish : PlayLiveCountDownTimerState()
}