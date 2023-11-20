package com.tokopedia.play.broadcaster.analytic.summary

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 10, 2022
 */
class PlayBroadcastSummaryAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayBroadcastSummaryAnalytic {

    private val shopId: String
        get() = userSession.shopId

    override fun clickPostingVideoOnReportPage() {
        sendClickEvent(
            "click - post to shop page",
            shopId
        )
    }

    override fun clickPostingVideoNow() {
        sendClickEvent(
            "click - post video now",
            shopId
        )
    }

    override fun clickContentTag(tagName: String, isChosen: Boolean) {
        val actionType = if (isChosen) "click" else "unclick"
        sendClickEvent(
            "$actionType - content tag",
            "$shopId - $tagName"
        )
    }

    override fun clickCoverOnReportPage(channelID: String, channelTitle: String) {
        sendClickEvent(
            "click - cover image on report page",
            "$shopId - $channelID - $channelTitle"
        )
    }

    override fun impressReportPage(channelID: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            "/seller broadcast - report summary - $shopId - $channelID",
            mapOf<String, String>(
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to currentSite,
                Key.isLoggedInStatus to userSession.isLoggedIn.toString()
            )
        )
    }

    override fun clickInteractiveParticipantDetail(channelID: String, channelTitle: String) {
        sendClickEvent(
            "click - lihat detail gamification",
            "$shopId - $channelID - $channelTitle"
        )
    }

    private fun sendClickEvent(
        eventAction: String,
        eventLabel: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to KEY_TRACK_CLICK_EVENT,
                Key.eventCategory to KEY_TRACK_CATEGORY,
                Key.eventAction to eventAction,
                Key.eventLabel to eventLabel,
                Key.currentSite to currentSite,
                Key.shopId to userSession.shopId,
                Key.userId to userSession.userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }
}
