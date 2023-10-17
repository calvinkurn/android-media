package com.tokopedia.stories.creation.analytic.product.seller

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.seller.analytic.ContentProductPickerSellerAnalytic
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
class StoriesCreationProductSellerAnalytic @Inject constructor(
    
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

    }

    /** Row 5 */
    override fun clickSelectProductOnProductSetup(productId: String) {

    }

    /** Row 6 */
    override fun clickSaveButtonOnProductSetup() {

    }

    /** Row 7 */
    override fun clickSearchBarOnProductSetup() {

    }

    /** Row 8 */
    override fun clickProductSorting() {

    }

    /** Row 9 */
    override fun clickSortingTypeItem(sortName: String) {

    }

    /** Row 10 */
    override fun clickCampaignAndEtalaseFilter() {

    }

    /** Row 11 */
    override fun clickCampaignCard(campaignName: String) {

    }

    /** Row 12 */
    override fun clickEtalaseCard(etalaseName: String) {

    }

    /** Row 13 */
    override fun viewProductSummary() {

    }

    /** Row 14 */
    override fun clickDeleteProductOnProductSetup(productId: String) {

    }

    /** Row 15 */
    override fun clickDoneOnProductSetup() {

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
