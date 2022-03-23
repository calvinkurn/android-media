package com.tokopedia.play.broadcaster.pusher.revamp.timer


/**
 * Created by mzennis on 17/03/21.
 */
sealed class PlayBroadcastTimerState {
    data class Active(val duration: Long) : PlayBroadcastTimerState()
    object Finish : PlayBroadcastTimerState()
}