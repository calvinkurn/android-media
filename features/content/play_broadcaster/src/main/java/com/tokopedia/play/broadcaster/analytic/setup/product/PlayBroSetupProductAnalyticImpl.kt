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

    override fun clickAddMoreProductOnProductSetup() {
        sendEvent(
            "click - add product card",
            "$shopId"
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

    override fun clickCampaignAndEtalaseFilter() {
        /** TODO: in thanos, the label should be "click - campaign & \netalase filter", with \n
         * but for now i remove it */
        sendEvent("click - campaign & etalase filter")
    }

    override fun clickProductSorting() {
        sendEvent("click - product sort")
    }

    override fun clickProductSortingType(sortName: String) {
        sendEvent(
            "click - sort type",
            "$shopId - $sortName"
        )
    }

    override fun clickEtalaseCard() {
        sendEvent("click - etalase card")
    }

    override fun clickCampaignCard() {
        sendEvent("click - campaign card")
    }

    override fun clickCloseOnProductChooser(isProductSelected: Boolean) {
        val productState = if(isProductSelected) "selected product list section" else "select product section"
        sendEvent(
            "click - close button on product bottom sheet",
            "$shopId - $productState"
        )
    }

    override fun clickConfirmCloseOnProductChooser() {
        sendEvent("click - confirm close on add product page")
    }

    override fun clickCancelCloseOnProductChooser() {
        sendEvent("click - cancel close on add product page")
    }

    private fun sendEvent(
        eventAction: String,
        eventLabel: String = shopId,
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to "clickSellerBroadcast",
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