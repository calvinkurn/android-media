package com.tokopedia.play.broadcaster.pusher


/**
 * Created by mzennis on 04/06/21.
 */
interface PlayLivePusherMediatorListener {

    fun onLivePusherStateChanged(state: PlayLivePusherMediatorState) { }

    fun onLivePusherStatsUpdated(statistic: PlayLivePusherStatistic) { }

    fun onLiveTimerActive(timeInMillis: Long) { }

    fun onLiveTimerFinish() { }

    fun onReachMaximumPausePeriod() { }
}