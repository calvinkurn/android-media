package com.tokopedia.play.analytic.interactive

import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.partner.PlayPartnerAnalyticImpl
import com.tokopedia.play.view.type.PlayChannelType
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

    override fun clickFollowShopInteractive(channelId: String, channelType: PlayChannelType) {
        sendCompleteGeneralEvent(
                event = KEY_TRACK_CLICK_GROUP_CHAT,
                eventCategory = KEY_TRACK_GROUP_CHAT_ROOM,
                eventAction = "click follow from engagement tools widget",
                eventLabel = "$channelId - ${channelType.value}"
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

    override fun clickTapTap(channelId: String, channelType: PlayChannelType) {
        sendCompleteGeneralEvent(
                event = KEY_TRACK_CLICK_GROUP_CHAT,
                eventCategory = KEY_TRACK_GROUP_CHAT_ROOM,
                eventAction = "click tap terus icon",
                eventLabel = "$channelId - ${channelType.value}"
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

    companion object {
        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_USER_ID = "userId"
        private const val KEY_BUSINESS_UNIT = "businessUnit"

        private const val KEY_TRACK_CLICK_GROUP_CHAT = "clickGroupChat"
        private const val KEY_TRACK_GROUP_CHAT_ROOM = "groupchat room"
        private const val KEY_TRACK_CURRENT_SITE = "tokopediamarketplace"
        private const val KEY_TRACK_BUSINESS_UNIT = "play"

    }
}