package com.tokopedia.play.broadcaster.analytic.setup.menu

import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.analytic.KEY_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_CURRENT_SITE
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_ACTION
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_LABEL
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_USER_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on February 04, 2022
 */
class PlayBroSetupMenuAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayBroSetupMenuAnalytic{

    private val shopId = userSession.shopId

    override fun clickSetupTitleMenu() {
        sendEvent("click - edit title")
    }

    override fun clickSetupCoverMenu() {
        sendEvent("click - add cover on preparation page")
    }

    override fun clickSetupProductMenu() {
        sendEvent("click - add product tag")
    }

    override fun clickSwitchCameraOnPreparation() {
        sendEvent("click - camera switch on preparation page")
    }

    override fun clickCloseOnPreparation() {
        sendEvent("click - x on preparation page")
    }

    override fun clickCancelStreaming(channelId: String, title: String) {
        sendEvent(
            "click - batalkan livestream",
            "$shopId - $channelId - $title"
        )
    }

    override fun clickStartStreaming(channelId: String) {
        sendEvent(
            "click - mulai live streaming",
            "$shopId - $channelId"
        )
    }

    private fun sendEvent(
        eventAction: String,
        eventLabel: String = shopId,
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to "clickPG",
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_LABEL to eventLabel,
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
            )
        )
    }
}