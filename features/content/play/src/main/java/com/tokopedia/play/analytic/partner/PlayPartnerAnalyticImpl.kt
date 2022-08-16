package com.tokopedia.play.analytic.partner

import com.tokopedia.play.analytic.*
import com.tokopedia.play.analytic.KEY_TRACK_CLICK
import com.tokopedia.play.analytic.KEY_TRACK_CLICK_GROUP_CHAT
import com.tokopedia.play.analytic.KEY_TRACK_CLICK_TOP_ADS
import com.tokopedia.play.analytic.KEY_TRACK_GROUP_CHAT_ROOM
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
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


    override fun clickFollowUniversal(channelId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_TOP_ADS)
            .setEventAction("$KEY_TRACK_CLICK - follow button")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .build()
            .send()
    }

    override fun impressFollow(channelId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_TOP_ADS)
            .setEventAction("impression - follow button")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .build()
            .send()
    }
}