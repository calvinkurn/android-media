package com.tokopedia.play.broadcaster.shorts.analytic.product

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalytic
import com.tokopedia.play.broadcaster.shorts.analytic.helper.PlayShortsAnalyticHelper
import com.tokopedia.play.broadcaster.shorts.analytic.sender.PlayShortsAnalyticSender
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 24, 2022
 */

/**
 * Mynakama Tracker
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3511
 */
class PlayShortsSetupProductAnalyticImpl @Inject constructor(
    private val analyticSender: PlayShortsAnalyticSender,
) : PlayBroSetupProductAnalytic {

    private var account = ContentAccountUiModel.Empty

    override fun setSelectedAccount(account: ContentAccountUiModel) {
        this.account = account
    }

    /**
     * Row 37
     */
    override fun clickSearchBarOnProductSetup() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - search product",
            account = account,
            trackerId = "37560",
        )
    }

    /**
     * Row 38
     */
    override fun clickProductSorting() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - product sort",
            account = account,
            trackerId = "37561",
        )
    }

    /**
     * Row 39
     */
    override fun clickCampaignAndEtalaseFilter() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - filter product etalase",
            account = account,
            trackerId = "37562",
        )
    }

    /**
     * Row 40
     */
    override fun clickCloseOnProductChooser(isProductSelected: Boolean) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - close button on product bottom sheet",
            account = account,
            trackerId = "37563",
        )
    }

    /**
     * Row 41
     */
    override fun clickSelectProductOnProductSetup(productId: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - product card",
            eventLabel = "${PlayShortsAnalyticHelper.getEventLabelByAccount(account)} - $productId",
            trackerId = "37564",
        )
    }

    /**
     * Row 42
     */
    override fun clickCloseOnProductSortingBottomSheet() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - close sort product",
            account = account,
            trackerId = "37565",
        )
    }

    /**
     * Row 43
     */
    override fun clickProductSortingType(sortName: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - sort type",
            account = account,
            trackerId = "37566",
        )
    }

    /**
     * Row 44
     */
    override fun viewProductSortingBottomSheet() {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - sorting bottom sheet",
            account = account,
            trackerId = "37567"
        )
    }

    /**
     * Row 45
     */
    override fun clickCloseOnProductFilterBottomSheet() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - close filter bottom sheet",
            account = account,
            trackerId = "37568",
        )
    }

    /**
     * Row 46
     */
    override fun clickCampaignCard(campaignName: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - campaign card",
            eventLabel = "${PlayShortsAnalyticHelper.getEventLabelByAccount(account)} - $campaignName",
            trackerId = "37569",
        )
    }

    /**
     * Row 47
     */
    override fun clickEtalaseCard(etalaseName: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - etalase card",
            eventLabel = "${PlayShortsAnalyticHelper.getEventLabelByAccount(account)} - $etalaseName",
            trackerId = "37570",
        )
    }

    /**
     * Row 48
     */
    override fun viewProductFilterBottomSheet() {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - filter bottom sheet",
            account = account,
            trackerId = "37571"
        )
    }

    /**
     * Row 49
     */
    override fun viewProductChooser() {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - product selection bottom sheet",
            account = account,
            trackerId = "37572"
        )
    }

    /**
     * Row 50
     */
    override fun clickSaveButtonOnProductSetup() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - save product card",
            eventLabel = "",
            trackerId = "37573",
        )
    }

    /**
     * Row 71
     */
    override fun clickAddMoreProductOnProductSetup() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - add product card",
            account = account,
            trackerId = "37594",
        )
    }

    /**
     * Row 72
     */
    override fun clickCloseOnProductSummary() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - back product selection page",
            account = account,
            trackerId = "37595",
        )
    }

    /**
     * Row 73
     */
    override fun clickDeleteProductOnProductSetup(productId: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - delete a product tagged",
            eventLabel = "${PlayShortsAnalyticHelper.getEventLabelByAccount(account)} - $productId",
            trackerId = "37596",
        )
    }

    /**
     * Row 74
     */
    override fun clickDoneOnProductSetup() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - save product tag",
            account = account,
            trackerId = "37597",
        )
    }

    /**
     * Row 75
     */
    override fun viewProductSummary() {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - product selection summary",
            account = account,
            trackerId = "37598"
        )
    }

    override fun clickConfirmCloseOnProductChooser() {
        /** Not applicable for shorts */
    }

    override fun clickCancelCloseOnProductChooser() {
        /** Not applicable for shorts */
    }

    override fun clickSearchWhenParamChanged(search: String) {
        /** Not applicable for shorts */
    }
}
