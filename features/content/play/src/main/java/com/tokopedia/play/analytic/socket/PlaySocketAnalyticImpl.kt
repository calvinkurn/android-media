package com.tokopedia.play.analytic.socket

import com.tokopedia.play.analytic.KEY_TRACK_GROUP_CHAT_ROOM
import com.tokopedia.play.analytic.KEY_TRACK_VIEW_GROUP_CHAT
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * Created by jegul on 25/08/21
 */
class PlaySocketAnalyticImpl @Inject constructor(
) : PlaySocketAnalytic {

    override fun socketError(channelId: String, channelType: PlayChannelType, error: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_VIEW_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "error state",
                "$channelId - Socket Connection: $error - ${channelType.value}"
        )
    }
}