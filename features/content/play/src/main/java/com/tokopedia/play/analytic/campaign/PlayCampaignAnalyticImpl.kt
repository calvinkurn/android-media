package com.tokopedia.play.analytic.campaign

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
        val eventAction =
            if (sectionInfo.config.reminder == PlayUpcomingBellStatus.On) "remove ${Companion.KEY_TRACK_UPCOMING_REMINDER}" else Companion.KEY_TRACK_UPCOMING_REMINDER
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_TOP_ADS,
                KEY_EVENT_ACTION to "$KEY_TRACK_CLICK - $eventAction",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$channelId - ${channelType.value} - ${sectionInfo.id}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
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
                KEY_EVENT to KEY_TRACK_VIEW_TOP_ADS,
                KEY_EVENT_ACTION to "impression - $KEY_TRACK_UPCOMING_REMINDER",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$channelId - ${channelType.value} - ${sectionInfo.id}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }

    companion object {
        private const val KEY_TRACK_UPCOMING_REMINDER = "remind me in upcoming section"
    }
}