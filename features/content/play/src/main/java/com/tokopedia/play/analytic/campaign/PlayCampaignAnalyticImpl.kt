package com.tokopedia.play.analytic.campaign

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.analytic.*
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.PlayUpcomingBellStatus
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 21/03/22
 */
class PlayCampaignAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayCampaignAnalytic {

    private val userId: String
        get() = userSession.userId

    override fun clickUpcomingReminder(
        sectionInfo: ProductSectionUiModel.Section,
        channelId: String,
        channelType: PlayChannelType
    ) {
        val eventAction = if (sectionInfo.config.reminder == PlayUpcomingBellStatus.On) {
            "remove remind me in upcoming section"
        } else {
            "remind me in upcoming section"
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickTopAds,
                Key.eventAction to "$KEY_TRACK_CLICK - $eventAction",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to "$channelId - ${channelType.value} - ${sectionInfo.id}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }

    override fun impressUpcomingReminder(
        sectionInfo: ProductSectionUiModel.Section,
        channelId: String,
        channelType: PlayChannelType
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.viewTopAdsIris,
                Key.eventAction to "impression - remind me in upcoming section",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to "$channelId - ${channelType.value} - ${sectionInfo.id}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }
}
