package com.tokopedia.play.analytic.share

import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.analytic.*
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 15, 2021
 */
class PlayShareExperienceAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayShareExperienceAnalytic {

    private val userId: String
        get() = if (userSession.userId.isNotEmpty()) userSession.userId else "0"

    private fun sendGeneralClickEvent(action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to KEY_TRACK_CLICK_COMMUNICATION,
                Key.eventAction to "click - $action",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to label,
                Key.businessUnit to KEY_TRACK_BUSINESS_UNIT_SHARE_EXPERIENCE,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }

    private fun sendGeneralViewEvent(action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to KEY_TRACK_VIEW_COMMUNICATION_IRIS,
                Key.eventAction to action,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to label,
                Key.businessUnit to KEY_TRACK_BUSINESS_UNIT_SHARE_EXPERIENCE,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }

    private fun mapPermissionLabel(label: String): String = when (label) {
        "allow", "deny" -> label.replaceFirstChar(Char::titlecase)
        else -> label
    }

    private fun getPartnerId(partnerId: Long?) = partnerId?.toString() ?: "0"

    override fun clickShareButton(channelId: String, partnerId: Long?, channelType: String) {
        sendGeneralClickEvent(
            "share button",
            "$channelId - ${getPartnerId(partnerId)} - $channelType"
        )
    }

    override fun closeShareBottomSheet(
        channelId: String,
        partnerId: Long?,
        channelType: String,
        isScreenshot: Boolean
    ) {
        val action =
            if (isScreenshot) "close screenshot share bottom sheet" else "close share bottom sheet"
        sendGeneralClickEvent(
            action,
            "$channelId - ${getPartnerId(partnerId)} - $channelType"
        )
    }

    override fun clickSharingOption(
        channelId: String,
        partnerId: Long?,
        channelType: String,
        sharingOption: String?,
        isScreenshot: Boolean
    ) {
        val action =
            if (isScreenshot) "channel share bottom sheet - screenshot" else "sharing channel"
        sendGeneralClickEvent(
            action,
            "$sharingOption - $channelId - ${getPartnerId(partnerId)} - $channelType"
        )
    }

    override fun impressShareBottomSheet(channelId: String, partnerId: Long?, channelType: String) {
        sendGeneralViewEvent(
            "view on sharing channel",
            "$channelId - ${getPartnerId(partnerId)} - $channelType"
        )
    }

    override fun clickSharePermission(
        channelId: String,
        partnerId: Long?,
        channelType: String,
        label: String
    ) {
        sendGeneralClickEvent(
            "access photo media and files",
            "${mapPermissionLabel(label)} - $channelId - ${getPartnerId(partnerId)} - $channelType"
        )
    }

    override fun takeScreenshotForSharing(
        channelId: String,
        partnerId: Long?,
        channelType: String
    ) {
        sendGeneralViewEvent(
            "view - screenshot share bottom sheet",
            "$channelId - ${getPartnerId(partnerId)} - $channelType"
        )
    }
}
