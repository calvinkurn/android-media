package com.tokopedia.play.analytic.socket

import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * Created by jegul on 25/08/21
 */
class PlaySocketAnalyticImpl @Inject constructor() : PlaySocketAnalytic {

    override fun socketError(channelId: String, channelType: PlayChannelType, error: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.viewGroupChat,
            EventCategory.groupChatRoom,
            "error state",
            "$channelId - Socket Connection: $error - ${channelType.value}"
        )
    }
}
