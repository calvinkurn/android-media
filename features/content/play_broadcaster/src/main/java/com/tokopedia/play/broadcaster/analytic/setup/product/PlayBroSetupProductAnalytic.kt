package com.tokopedia.play.broadcaster.analytic.setup.product

/**
 * Created By : Jonathan Darwin on February 04, 2022
 */
interface PlayBroSetupProductAnalytic {

    fun clickSearchBarOnProductSetup(search: String)

    fun clickSaveButtonOnProductSetup(productId: String)

    fun clickAddMoreProductOnProductSetup()

    fun clickSelectProductOnProductSetup(productId: String)

    fun clickDeleteProductOnProductSetup(productId: String)

    fun clickDoneOnProductSetup()

    fun clickCampaignAndEtalaseFilter()

    fun clickProductSorting()

    fun clickProductSortingType(sortName: String)

    fun clickEtalaseCard()

    fun clickCampaignCard()

    fun clickCloseOnProductChooser(isProductSelected: Boolean)

    fun clickConfirmCloseOnProductChooser()

    fun clickCancelCloseOnProductChooser()
}