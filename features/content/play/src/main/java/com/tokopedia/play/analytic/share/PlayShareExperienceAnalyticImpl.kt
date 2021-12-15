package com.tokopedia.play.analytic.share

import com.tokopedia.play.analytic.*
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 15, 2021
 */
class PlayShareExperienceAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
): PlayShareExperienceAnalytic{

    private val userId: String
        get() = userSession.userId

    private fun sendGeneralClickEvent(action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_EVENT_ACTION to "click - $action",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to label,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT_SHARE_EXPERIENCE,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }

    private fun sendGeneralViewEvent(action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_GROUP_CHAT,
                KEY_EVENT_ACTION to action,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to label,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT_SHARE_EXPERIENCE,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }

    private fun mapSharingOption(sharingOption: String?): String {
        return sharingOption?.let {
            return@let when(it) {
                "FB Feed" -> "Facebook NewsFeed"
                "FB Story" -> "Facebook Story"
                "IG DM" -> "Instagram DirectMessage"
                "IG Feed" -> "Instagram Feed"
                "IG Story" -> "Instagram Story"
                "E-mail" -> "Email"
                else -> it
            }
        } ?: ""
    }

    override fun clickShareButton(channelId: String, channelType: String) {
        sendGeneralClickEvent(
            "share button",
            "$channelId - $channelType"
        )
    }

    override fun closeShareBottomSheet(
        channelId: String,
        channelType: String,
        isScreenshot: Boolean
    ) {
        val action = if(isScreenshot) "close screenshot share bottom sheet" else "close share bottom sheet"
        sendGeneralClickEvent(
            action,
            "$channelId - $channelType"
        )
    }

    override fun clickSharingOption(
        channelId: String,
        channelType: String,
        sharingOption: String?,
        isScreenshot: Boolean
    ) {
        val action = if(isScreenshot) "channel share bottom sheet - screenshot" else "sharing channel"
        sendGeneralClickEvent(
            action,
            "$channelId - $channelType - ${mapSharingOption(sharingOption)}"
        )
    }

    override fun impressShareBottomSheet(channelId: String, channelType: String) {
        sendGeneralViewEvent(
            "view on sharing channel",
            "$channelId - $channelType"
        )
    }

    override fun clickSharePermission(channelId: String, channelType: String, isAllow: Boolean) {
        val status = if(isAllow) "allow" else "deny"
        sendGeneralClickEvent(
            "access photo media and files",
            "$channelId - $channelType - $status"
        )
    }

    override fun takeScreenshotForSharing(channelId: String, channelType: String) {
        sendGeneralViewEvent(
            "view - screenshot share bottom sheet",
            "$channelId - $channelType"
        )
    }
}