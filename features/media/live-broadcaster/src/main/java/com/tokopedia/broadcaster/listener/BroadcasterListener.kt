package com.tokopedia.broadcaster.listener

import com.tokopedia.broadcaster.tracker.BroadcasterDataLog
import com.tokopedia.broadcaster.state.BroadcasterState

interface BroadcasterListener {
    fun onNewLivePusherState(state: BroadcasterState)
    fun onUpdateLivePusherStatistic(log: BroadcasterDataLog)
}