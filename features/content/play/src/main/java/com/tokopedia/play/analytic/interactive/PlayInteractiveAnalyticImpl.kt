package com.tokopedia.play.analytic.interactive

import com.tokopedia.play.analytic.*
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.PlayUpcomingBellStatus
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 09/07/21
 */
class PlayInteractiveAnalyticImpl @Inject constructor(
        private val userSession: UserSessionInterface
) : PlayInteractiveAnalytic {

    private val userId: String
        get() = userSession.userId

    override fun clickFollowShopInteractive(
        channelId: String,
        channelType: PlayChannelType,
        interactiveId: String
    ) {
        sendCompleteGeneralEvent(
                event = KEY_TRACK_CLICK_GROUP_CHAT,
                eventCategory = KEY_TRACK_GROUP_CHAT_ROOM,
                eventAction = "click follow from engagement tools widget",
                eventLabel = "$channelId - ${channelType.value} - $interactiveId"
        )
    }

    override fun clickWinnerBadge(channelId: String, channelType: PlayChannelType) {
        sendCompleteGeneralEvent(
                event = KEY_TRACK_CLICK_GROUP_CHAT,
                eventCategory = KEY_TRACK_GROUP_CHAT_ROOM,
                eventAction = "click daftar pemenang on engagement tools widget",
                eventLabel = "$channelId - ${channelType.value}"
        )
    }

    override fun clickTapTap(
        channelId: String,
        channelType: PlayChannelType,
        interactiveId: String
    ) {
        sendCompleteGeneralEvent(
                event = KEY_TRACK_CLICK_GROUP_CHAT,
                eventCategory = KEY_TRACK_GROUP_CHAT_ROOM,
                eventAction = "click tap terus icon",
                eventLabel = "$channelId - ${channelType.value} - $interactiveId"
        )
    }

    override fun clickRefreshLeaderBoard(
        channelId: String,
        interactiveId: String,
        shopId: String
    ) {
        sendCompleteGeneralEvent(
            event = KEY_TRACK_CLICK_CONTENT,
            eventCategory = KEY_TRACK_GROUP_CHAT_ROOM,
            eventAction = "click - refresh button leaderboard bottomsheet",
            eventLabel = "$shopId - $channelId - $userId - $interactiveId"
        )
    }

    override fun clickQuizOption(
        channelId: String,
        choiceAlphabet: String,
        interactiveId: String,
        shopId: String
    ) {
        sendCompleteGeneralEvent(
            event = KEY_TRACK_CLICK_CONTENT,
            eventCategory = KEY_TRACK_GROUP_CHAT_ROOM,
            eventAction = "click - multiple choice quiz popup",
            eventLabel = "$shopId - $channelId - $userId - $interactiveId - $choiceAlphabet"
        )
    }

    override fun clickActiveInteractive(
        channelId: String,
        interactiveId: String,
        shopId: String
    ) {
        sendCompleteGeneralEvent(
            event = KEY_TRACK_CLICK_CONTENT,
            eventCategory = KEY_TRACK_GROUP_CHAT_ROOM,
            eventAction = "click - quiz widget",
            eventLabel = "$shopId - $channelId - $userId - $interactiveId"
        )
    }

    private fun sendCompleteGeneralEvent(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to event,
                        KEY_EVENT_CATEGORY to eventCategory,
                        KEY_EVENT_ACTION to eventAction,
                        KEY_EVENT_LABEL to eventLabel,
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }
}