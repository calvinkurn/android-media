package com.tokopedia.play.analytic.partner

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.play.analytic.*
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import javax.inject.Inject

/**
 * Created by jegul on 06/07/21
 */
class PlayPartnerAnalyticImpl @Inject constructor() : PlayPartnerAnalytic {

    override fun clickFollowShop(
        channelId: String,
        channelType: PlayChannelType,
        shopId: String,
        action: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.clickGroupChat,
            EventCategory.groupChatRoom,
            "click $action shop",
            "$channelId - $shopId - ${channelType.value}"
        )
    }

    override fun clickShop(channelId: String, channelType: PlayChannelType, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.clickGroupChat,
            EventCategory.groupChatRoom,
            "click - shop",
            "$shopId - $channelId - ${channelType.value}"
        )
    }

    override fun clickFollowUniversal(channelId: String) {
        Tracker.Builder()
            .setEvent(Event.clickTopAds)
            .setEventAction("click - follow button")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .build()
            .send()
    }

    override fun impressFollow(channelId: String) {
        Tracker.Builder()
            .setEvent(Event.viewTopAdsIris)
            .setEventAction("impression - follow button")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .build()
            .send()
    }
}
