package com.tokopedia.play.broadcaster.util.logger

import com.tokopedia.play.broadcaster.data.type.PlaySocketType
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorState
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus


/**
 * Created by mzennis on 21/10/21.
 */
interface PlayLogger {

    fun logChannelStatus(
        channelStatus: ChannelStatus
    )

    fun logPusherState(
        pusherState: PlayLivePusherMediatorState
    )

    fun logSocketType(
        socketType: PlaySocketType
    )

    fun sendAll(channelId: String)

}