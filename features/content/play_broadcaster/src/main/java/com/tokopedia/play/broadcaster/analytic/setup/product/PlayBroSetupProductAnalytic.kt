package com.tokopedia.play.broadcaster.analytic.setup.product

/**
 * Created By : Jonathan Darwin on February 04, 2022
 */
interface PlayBroSetupProductAnalytic {

    fun clickSearchBarOnProductSetup(search: String)

    fun clickSaveButtonOnProductSetup(productId: String)

    fun clickAddMoreProductonProductSetup(productId: String) /** TODO: where to get this productId? */

    fun clickSelectProductOnProductSetup(productId: String)

    fun clickDeleteProductOnProductSetup(productId: String)

    fun clickDoneOnProductSetup()

    fun clickCampaignAndEtalaseFilter()

    fun clickProductSorting()

    fun clickProductSortingType(sortName: String)

    fun clickCampaignAndEtalase()
}