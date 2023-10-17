package com.tokopedia.stories.creation.analytic.product.seller

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.seller.analytic.ContentProductPickerSellerAnalytic
import com.tokopedia.stories.creation.analytic.helper.StoriesCreationAnalyticHelper
import com.tokopedia.stories.creation.analytic.sender.StoriesCreationAnalyticSender
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
class StoriesCreationProductSellerAnalytic @Inject constructor(
    private val analyticSender: StoriesCreationAnalyticSender
) : ContentProductPickerSellerAnalytic {

    /**
     * MyNakama
     * MA : https://mynakama.tokopedia.com/datatracker/requestdetail/view/4305
     * SA : https://mynakama.tokopedia.com/datatracker/requestdetail/view/4326
     */

    private var account = ContentAccountUiModel.Empty

    override fun setSelectedAccount(account: ContentAccountUiModel) {
        this.account = account
    }

    /** Row 4 */
    override fun viewProductChooser() {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - product selection bottom sheet",
            account = account,
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47816", "47934")
        )
    }

    /** Row 5 */
    override fun clickSelectProductOnProductSetup(productId: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - product",
            eventLabel = "${StoriesCreationAnalyticHelper.getEventLabelByAccount(account)} - $productId",
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47817", "47935")
        )
    }

    /** Row 6 */
    override fun clickSaveButtonOnProductSetup() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - save product selection",
            account = account,
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47818", "47936")
        )
    }

    /** Row 7 */
    override fun clickSearchBarOnProductSetup() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - search product",
            account = account,
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47820", "47937")
        )
    }

    /** Row 8 */
    override fun clickProductSorting() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - sort product",
            account = account,
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47821", "47938")
        )
    }

    /** Row 9 */
    override fun clickSortingTypeItem(sortName: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - sorting option",
            eventLabel = "${StoriesCreationAnalyticHelper.getEventLabelByAccount(account)} - $sortName",
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47823", "47939")
        )
    }

    /** Row 10 */
    override fun clickCampaignAndEtalaseFilter() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - filter product",
            account = account,
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47825", "47940")
        )
    }

    /** Row 11 */
    override fun clickCampaignCard(campaignName: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - campaign option",
            eventLabel = "${StoriesCreationAnalyticHelper.getEventLabelByAccount(account)} - $campaignName",
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47827", "47941")
        )
    }

    /** Row 12 */
    override fun clickEtalaseCard(etalaseName: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - etalase option",
            eventLabel = "${StoriesCreationAnalyticHelper.getEventLabelByAccount(account)} - $etalaseName",
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47828", "47942")
        )
    }

    /** Row 13 */
    override fun viewProductSummary() {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - product selection summary bottom sheet",
            account = account,
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47830", "47943")
        )
    }

    /** Row 14 */
    override fun clickDeleteProductOnProductSetup(productId: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - delete selected product",
            eventLabel = "${StoriesCreationAnalyticHelper.getEventLabelByAccount(account)} - $productId",
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47833", "47944")
        )
    }

    /** Row 15 */
    override fun clickDoneOnProductSetup() {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - save selesai product selection",
            account = account,
            trackerId = StoriesCreationAnalyticHelper.getTrackerIdBySite("47834", "47945")
        )
    }

    override fun clickSearchWhenParamChanged(search: String) {
        /** Not applicable for stories */
    }

    override fun clickAddMoreProductOnProductSetup() {
        /** Not applicable for stories */
    }

    override fun clickProductSortingType(sortName: String) {
        /** Not applicable for stories */
    }

    override fun clickCloseOnProductChooser(isProductSelected: Boolean) {
        /** Not applicable for stories */
    }

    override fun clickConfirmCloseOnProductChooser() {
        /** Not applicable for stories */
    }

    override fun clickCancelCloseOnProductChooser() {
        /** Not applicable for stories */
    }

    override fun clickCloseOnProductSummary() {
        /** Not applicable for stories */
    }

    override fun clickCloseOnProductSortingBottomSheet() {
        /** Not applicable for stories */
    }

    override fun viewProductSortingBottomSheet() {
        /** Not applicable for stories */
    }

    override fun clickCloseOnProductFilterBottomSheet() {
        /** Not applicable for stories */
    }

    override fun viewProductFilterBottomSheet() {
        /** Not applicable for stories */
    }
}
