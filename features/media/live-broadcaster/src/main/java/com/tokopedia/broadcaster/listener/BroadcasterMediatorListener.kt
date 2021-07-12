package com.tokopedia.broadcaster.listener

import com.tokopedia.broadcaster.data.BroadcasterLogger
import com.tokopedia.broadcaster.state.BroadcasterState

interface BroadcasterMediatorListener {
    fun onLivePusherStateChanged(state: BroadcasterState)
    fun onLivePusherStatsUpdated(statistic: BroadcasterLogger)
    fun onReachMaxPauseDuration()
    fun onShouldContinueLiveStreaming()
}