package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.broadcaster.mediator.LivePusherStatistic


/**
 * Created by mzennis on 04/06/21.
 */
interface PlayLivePusherMediatorListener {

    fun onLivePusherStateChanged(state: PlayLivePusherMediatorState) { }

    fun onLivePusherStatsUpdated(statistic: LivePusherStatistic) { }

    fun onLiveTimerActive(timeInMillis: Long) { }

    fun onLiveTimerFinish() { }

    fun onReachMaximumPausePeriod() { }
}