package com.tokopedia.play.broadcaster.analytic.setup.menu

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on February 04, 2022
 */
class PlayBroSetupMenuAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayBroSetupMenuAnalytic {

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
        eventLabel: String = shopId
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to KEY_TRACK_CLICK_EVENT,
                Key.eventAction to eventAction,
                Key.eventCategory to KEY_TRACK_CATEGORY,
                Key.eventLabel to eventLabel,
                Key.currentSite to currentSite,
                Key.userId to userSession.userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }
}
