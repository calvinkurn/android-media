package com.tokopedia.play.broadcaster.analytic.setup.title

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CLICK_EVENT
import com.tokopedia.play.broadcaster.analytic.currentSite
import com.tokopedia.play.broadcaster.analytic.getTrackerId
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on February 04, 2022
 */
class PlayBroSetupTitleAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayBroSetupTitleAnalytic {

    override fun clickSubmitTitle() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to KEY_TRACK_CLICK_EVENT,
                Key.eventAction to "click - simpan",
                Key.eventCategory to KEY_TRACK_CATEGORY,
                Key.eventLabel to userSession.shopId,
                Key.currentSite to currentSite,
                Key.userId to userSession.userId,
                Key.businessUnit to BusinessUnit.play,
                Key.trackerId to getTrackerId("30206", "26705")
            )
        )
    }
}
