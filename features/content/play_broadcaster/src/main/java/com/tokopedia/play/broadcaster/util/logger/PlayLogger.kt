package com.tokopedia.play.broadcaster.util.logger

import com.tokopedia.play.broadcaster.data.type.PlaySocketType
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorState
import com.tokopedia.play_common.types.PlayChannelStatusType


/**
 * Created by mzennis on 21/10/21.
 */
interface PlayLogger {

    fun logChannelStatus(
        channelStatus: PlayChannelStatusType
    )

    fun logPusherStatus(
        pusherState: PlayLivePusherMediatorState
    )

    fun logSocketStatus(
        socketType: PlaySocketType
    )

    /**
     * Important! send log when onPause()
     */
    fun sendAll(channelId: String)

}