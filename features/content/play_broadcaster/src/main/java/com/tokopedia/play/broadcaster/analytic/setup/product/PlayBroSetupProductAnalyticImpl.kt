package com.tokopedia.play.broadcaster.analytic.setup.product

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
class PlayBroSetupProductAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayBroSetupProductAnalytic {

    private val shopId = userSession.shopId

    override fun clickSearchBarOnProductSetup(search: String) {
        sendEvent(
            "click - search bar",
            "$shopId - $search"
        )
    }

    override fun clickSaveButtonOnProductSetup(productId: String) {
        sendEvent(
            "click - save product card",
            "$shopId - $productId"
        )
    }

    override fun clickAddMoreProductonProductSetup(productId: String) {
        sendEvent(
            "click - add product card",
            "$shopId - $productId"
        )
    }

    override fun clickSelectProductOnProductSetup(productId: String) {
        sendEvent(
            "click - product card",
            "$shopId - $productId"
        )
    }

    override fun clickDeleteProductOnProductSetup(productId: String) {
        sendEvent(
            "click - delete a product tagged",
            "$shopId - $productId"
        )
    }

    override fun clickDoneOnProductSetup() {
        sendEvent("click - save product tag")
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