package com.tokopedia.play.broadcaster.analytic.setup.product

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY
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

    override fun setSelectedAccount(account: ContentAccountUiModel) {
        /** Not applicable for broadcaster */
    }

    override fun clickSearchBarOnProductSetup() {
        /** Not applicable for broadcaster */
    }

    override fun clickSearchWhenParamChanged(search: String) {
        sendEvent(
            "click - search bar",
            "$shopId - $search"
        )
    }

    override fun clickSaveButtonOnProductSetup() {
        sendEvent(
            "click - save product card",
            shopId
        )
    }

    override fun clickAddMoreProductOnProductSetup() {
        sendEvent(
            "click - add product card",
            shopId
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

    override fun clickEtalaseCard(etalaseName: String) {
        sendEvent("click - etalase card")
    }

    override fun clickCampaignCard(campaignName: String) {
        sendEvent("click - campaign card")
    }

    override fun clickCloseOnProductChooser(isProductSelected: Boolean) {
        val productState = if (isProductSelected) "selected product list section" else "select product section"
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

    override fun clickCloseOnProductSummary() {
        /** Not applicable for broadcaster */
    }

    override fun viewProductSummary() {
        /** Not applicable for broadcaster */
    }

    override fun clickCloseOnProductSortingBottomSheet() {
        /** Not applicable for broadcaster */
    }

    override fun viewProductSortingBottomSheet() {
        /** Not applicable for broadcaster */
    }

    override fun clickCloseOnProductFilterBottomSheet() {
        /** Not applicable for broadcaster */
    }

    override fun viewProductFilterBottomSheet() {
        /** Not applicable for broadcaster */
    }

    override fun viewProductChooser() {
        /** Not applicable for broadcaster */
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
