package com.tokopedia.play.broadcaster.analytic.setup.cover

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
class PlayBroSetupCoverAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayBroSetupCoverAnalytic {

    override fun clickAddNewCover() {
        sendEvent("click - cover thumbnail to add cover")
    }

    override fun clickEditCover() {
        sendEvent("click - edit cover")
    }

    private fun sendEvent(
        eventAction: String,
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to "clickPG",
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_LABEL to userSession.shopId,
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
            )
        )
    }
}