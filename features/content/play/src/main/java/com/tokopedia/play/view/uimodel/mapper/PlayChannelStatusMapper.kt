package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.ChannelStatusResponse
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import javax.inject.Inject


/**
 * Created by mzennis on 04/02/21.
 */
@PlayScope
class PlayChannelStatusMapper @Inject constructor() {

    fun mapStatusFromResponse(channelStatus: ChannelStatusResponse) = PlayStatusType.getByValue(
            channelStatus.playGetChannelsStatus.data.firstOrNull()?.status.orEmpty()
    )

    fun mapWaitingDurationResponse(channelStatus: ChannelStatusResponse) =
        channelStatus.playGetChannelsStatus.waitingDuration

    fun mapStatusBanned(isBanned: Boolean) = if (isBanned) PlayStatusType.Banned else PlayStatusType.Active
}