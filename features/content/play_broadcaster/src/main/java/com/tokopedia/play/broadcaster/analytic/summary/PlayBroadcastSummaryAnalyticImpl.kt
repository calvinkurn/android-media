package com.tokopedia.play.broadcaster.analytic.summary

import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.analytic.KEY_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_CURRENT_SITE
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_ACTION
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_LABEL
import com.tokopedia.play.broadcaster.analytic.KEY_SHOP_ID
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_USER_ID
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
            shopId,
        )
    }

    override fun clickPostingVideoNow() {
        sendClickEvent(
            "click - post video now",
            shopId,
        )
    }

    override fun clickContentTag(tagName: String) {
        sendClickEvent(
            "click - content tag",
            "$shopId - $tagName",
        )
    }

    override fun clickCoverOnReportPage(channelID: String, channelTitle: String) {
        sendClickEvent(
            "click - cover image on report page",
            "$shopId - $channelID - $channelTitle",
        )
    }

    override fun impressReportPage(channelID: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_OPEN_SCREEN,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to currentSite,
                KEY_IS_LOGGED_IN_STATUS to userSession.isLoggedIn,
                KEY_SCREEN_NAME to "/seller broadcast - report summary - $shopId - $channelID",
                KEY_USER_ID to userSession.userId,
            )
        )
    }

    private fun sendClickEvent(
        eventAction: String,
        eventLabel: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel,
                KEY_CURRENT_SITE to currentSite,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
            )
        )
    }
}