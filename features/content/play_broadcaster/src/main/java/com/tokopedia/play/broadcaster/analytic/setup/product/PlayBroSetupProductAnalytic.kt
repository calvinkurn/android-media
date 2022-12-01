package com.tokopedia.play.broadcaster.analytic.setup.product

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on February 04, 2022
 */
interface PlayBroSetupProductAnalytic {

    fun setSelectedAccount(account: ContentAccountUiModel)

    fun clickSearchWhenParamChanged(search: String)

    fun clickSearchBarOnProductSetup()

    fun clickSaveButtonOnProductSetup()

    fun clickAddMoreProductOnProductSetup()

    fun clickSelectProductOnProductSetup(productId: String)

    fun clickDeleteProductOnProductSetup(productId: String)

    fun clickDoneOnProductSetup()

    fun clickCampaignAndEtalaseFilter()

    fun clickProductSorting()

    fun clickProductSortingType(sortName: String)

    fun clickEtalaseCard(etalaseName: String)

    fun clickCampaignCard(campaignName: String)

    fun clickCloseOnProductChooser(isProductSelected: Boolean)

    fun clickConfirmCloseOnProductChooser()

    fun clickCancelCloseOnProductChooser()

    fun clickCloseOnProductSummary()

    fun viewProductSummary()

    fun clickCloseOnProductSortingBottomSheet()

    fun viewProductSortingBottomSheet()

    fun clickCloseOnProductFilterBottomSheet()

    fun viewProductFilterBottomSheet()

    fun viewProductChooser()
}
