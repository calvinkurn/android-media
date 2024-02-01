package com.tokopedia.stories.creation.analytic.product.seller

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.content.analytic.model.ContentAnalyticAuthor
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.seller.analytic.ContentProductPickerSellerAnalytic
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
class StoriesCreationProductSellerAnalytic @Inject constructor(
    analyticManagerFactory: ContentAnalyticManager.Factory,
) : ContentProductPickerSellerAnalytic {

    /**
     * MyNakama
     * MA : https://mynakama.tokopedia.com/datatracker/requestdetail/view/4305
     * SA : https://mynakama.tokopedia.com/datatracker/requestdetail/view/4326
     */

    private var author: ContentAnalyticAuthor = ContentAnalyticAuthor.Empty

    private val analyticManager = analyticManagerFactory.create(
        businessUnit = BusinessUnit.content,
        eventCategory = EventCategory.storyCreation,
    )

    override fun setSelectedAccount(account: ContentAccountUiModel) {
        author = account.toAnalyticModel()
    }

    /** Row 4 */
    override fun viewProductChooser() {
        analyticManager.sendViewContent(
            eventAction = "view - product selection bottom sheet",
            eventLabel = analyticManager.concatLabelsWithAuthor(author),
            mainAppTrackerId = "47816",
            sellerAppTrackerId = "47934"
        )
    }

    /** Row 5 */
    override fun clickSelectProductOnProductSetup(productId: String) {
        analyticManager.sendClickContent(
            eventAction = "click - product",
            eventLabel = analyticManager.concatLabelsWithAuthor(author, productId),
            mainAppTrackerId = "47817",
            sellerAppTrackerId = "47935"
        )
    }

    /** Row 6 */
    override fun clickSaveButtonOnProductSetup() {
        analyticManager.sendClickContent(
            eventAction = "click - save product selection",
            eventLabel = analyticManager.concatLabelsWithAuthor(author),
            mainAppTrackerId = "47818",
            sellerAppTrackerId = "47818"
        )
    }

    /** Row 7 */
    override fun clickSearchBarOnProductSetup() {
        analyticManager.sendClickContent(
            eventAction = "click - search product",
            eventLabel = analyticManager.concatLabelsWithAuthor(author),
            mainAppTrackerId = "47820",
            sellerAppTrackerId = "47937"
        )
    }

    /** Row 8 */
    override fun clickProductSorting() {
        analyticManager.sendClickContent(
            eventAction = "click - sort product",
            eventLabel = analyticManager.concatLabelsWithAuthor(author),
            mainAppTrackerId = "47821",
            sellerAppTrackerId = "47938"
        )
    }

    /** Row 9 */
    override fun clickSortingTypeItem(sortName: String) {
        analyticManager.sendClickContent(
            eventAction = "click - sorting option",
            eventLabel = analyticManager.concatLabelsWithAuthor(author, sortName),
            mainAppTrackerId = "47823",
            sellerAppTrackerId = "47939"
        )
    }

    /** Row 10 */
    override fun clickCampaignAndEtalaseFilter() {
        analyticManager.sendClickContent(
            eventAction = "click - filter product",
            eventLabel = analyticManager.concatLabelsWithAuthor(author),
            mainAppTrackerId = "47825",
            sellerAppTrackerId = "47940"
        )
    }

    /** Row 11 */
    override fun clickCampaignCard(campaignName: String) {
        analyticManager.sendClickContent(
            eventAction = "click - campaign option",
            eventLabel = analyticManager.concatLabelsWithAuthor(author, campaignName),
            mainAppTrackerId = "47827",
            sellerAppTrackerId = "47941"
        )
    }

    /** Row 12 */
    override fun clickEtalaseCard(etalaseName: String) {
        analyticManager.sendClickContent(
            eventAction = "click - etalase option",
            eventLabel = analyticManager.concatLabelsWithAuthor(author, etalaseName),
            mainAppTrackerId = "47828",
            sellerAppTrackerId = "47942"
        )
    }

    /** Row 13 */
    override fun viewProductSummary() {
        analyticManager.sendViewContent(
            eventAction = "view - product selection summary bottom sheet",
            eventLabel = analyticManager.concatLabelsWithAuthor(author),
            mainAppTrackerId = "47830",
            sellerAppTrackerId = "47943"
        )
    }

    /** Row 14 */
    override fun clickDeleteProductOnProductSetup(productId: String) {
        analyticManager.sendClickContent(
            eventAction = "click - delete selected product",
            eventLabel = analyticManager.concatLabelsWithAuthor(author, productId),
            mainAppTrackerId = "47833",
            sellerAppTrackerId = "47944"
        )
    }

    /** Row 15 */
    override fun clickDoneOnProductSetup() {
        analyticManager.sendClickContent(
            eventAction = "click - save selesai product selection",
            eventLabel = analyticManager.concatLabelsWithAuthor(author),
            mainAppTrackerId = "47834",
            sellerAppTrackerId = "47945"
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
