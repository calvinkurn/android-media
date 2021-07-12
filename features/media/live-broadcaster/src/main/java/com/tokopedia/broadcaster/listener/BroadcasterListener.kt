package com.tokopedia.broadcaster.listener

import com.tokopedia.broadcaster.data.BroadcasterLogger
import com.tokopedia.broadcaster.state.BroadcasterState

interface BroadcasterListener {
    fun onNewLivePusherState(pusherState: BroadcasterState)
    fun onUpdateLivePusherStatistic(pusherStatistic: BroadcasterLogger)
}