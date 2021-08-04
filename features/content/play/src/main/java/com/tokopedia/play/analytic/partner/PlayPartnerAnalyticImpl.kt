package com.tokopedia.play.analytic.partner

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * Created by jegul on 06/07/21
 */
class PlayPartnerAnalyticImpl @Inject constructor() : PlayPartnerAnalytic {

    override fun clickFollowShop(channelId: String, channelType: PlayChannelType, shopId: String, action: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK $action shop",
                "$channelId - $shopId - ${channelType.value}"
        )
    }

    override fun clickShop(channelId: String, channelType: PlayChannelType, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK - shop",
                "$shopId - $channelId - ${channelType.value}"
        )
    }

    companion object {
        private const val KEY_TRACK_CLICK_GROUP_CHAT = "clickGroupChat"

        private const val KEY_TRACK_CLICK = "click"
        private const val KEY_TRACK_GROUP_CHAT_ROOM = "groupchat room"
    }
}