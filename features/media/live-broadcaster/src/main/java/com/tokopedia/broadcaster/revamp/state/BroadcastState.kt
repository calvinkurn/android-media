package com.tokopedia.broadcaster.revamp.state

import com.tokopedia.broadcaster.revamp.util.error.BroadcasterException

/**
 * Created by meyta.taliti on 19/03/22.
 */
sealed class BroadcastState {
    object Unprepared: BroadcastState()
    object Started: BroadcastState()
    object Recovered: BroadcastState()
    object Stopped: BroadcastState()
    data class Error(val cause: BroadcasterException): BroadcastState()
}