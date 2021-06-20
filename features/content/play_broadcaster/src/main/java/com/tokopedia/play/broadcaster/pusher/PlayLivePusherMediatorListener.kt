package com.tokopedia.play.broadcaster.pusher


/**
 * Created by mzennis on 04/06/21.
 */
interface PlayLivePusherMediatorListener {

    fun onLivePusherStateChanged(state: PlayLivePusherState) { }

    fun onLivePusherStatsUpdated(statistic: PlayLivePusherStatistic) { }

    fun onReachMaxPauseDuration() { }

    fun onShouldContinueLiveStreaming() { }
}