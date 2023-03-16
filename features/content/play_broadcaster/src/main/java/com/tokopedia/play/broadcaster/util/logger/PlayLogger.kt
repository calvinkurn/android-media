package com.tokopedia.play.broadcaster.util.logger

import com.tokopedia.play.broadcaster.data.type.PlaySocketType
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.pusher.statistic.PlayBroadcasterMetric
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus


/**
 * Created by mzennis on 21/10/21.
 */
interface PlayLogger {

    fun logChannelStatus(
        channelStatus: ChannelStatus
    )

    fun logPusherState(
        pusherState: PlayBroadcasterState
    )

    fun logSocketType(
        socketType: PlaySocketType
    )

    fun logBroadcastError(
        throwable: Throwable
    )

    fun sendAll(channelId: String)

    fun sendBroadcasterLog(metric: PlayBroadcasterMetric)

}