package com.tokopedia.broadcaster.revamp.state

/**
 * Created by meyta.taliti on 19/03/22.
 */
sealed class BroadcastState {
    object Unprepared: BroadcastState()
    object Started: BroadcastState()
    object Stopped: BroadcastState()
    data class Error(val cause: Throwable): BroadcastState()
}