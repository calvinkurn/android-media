package com.tokopedia.product.manage.feature.campaignstock

import com.tokopedia.product.manage.data.createShopOwnerAccess
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import org.junit.Test

class CampaignMainStockViewModelTest: CampaignMainStockViewModelTestFixture() {

    @Test
    fun `setting non variant stock lower than max stock will set the save button to enabled`() {
        viewModel.setNonVariantStock("1", 1, 2)

        assert(viewModel.shouldEnableSaveButton.value == true)
    }

    @Test
    fun `setting non variant stock higher than max stock will set the save button to disabled`() {
        viewModel.setNonVariantStock("1", 5, 2)

        assert(viewModel.shouldEnableSaveButton.value == false)
    }

    @Test
    fun `setting non variant stock with max stock null should set the save button to enabled`() {
        viewModel.setNonVariantStock("1", 1, null)

        assert(viewModel.shouldEnableSaveButton.value == true)
    }

    @Test
    fun `when save button has been disabled, setting non variant stock higher than max stock should not set the save button`() {
        viewModel.setNonVariantStock("1", 5, 2)
        val expectedValue = viewModel.shouldEnableSaveButton.value

        viewModel.setNonVariantStock("1", 10, 2)

        assert(viewModel.shouldEnableSaveButton.value == expectedValue)
    }

    @Test
    fun `setting variant stock should set the warning show conditions`() {
        viewModel.setVariantStock("1", 1)

        assert(viewModel.shouldDisplayVariantStockWarningLiveData.value != null)
    }

    @Test
    fun `setting variant product with empty stock will set the warning to be displayed`() {
        viewModel.setVariantStock("2", 0)

        assert(viewModel.shouldDisplayVariantStockWarningLiveData.value == true)
    }

    @Test
    fun `setting variant product with stock lower than max stock will set the save button to enabled`() {
        viewModel.setVariantStock("1", 5, 10)

        assert(viewModel.shouldEnableSaveButton.value == true)
    }

    @Test
    fun `setting variant product with stock higher than max stock will set the save button to disabled`() {
        viewModel.setVariantStock("1", 5, 1)

        assert(viewModel.shouldEnableSaveButton.value == false)
    }

    @Test
    fun `setting variant product with non-empty stock will set the warning to be not displayed`() {
        val productId = "3"

        val sellableProductList = listOf(
            SellableStockProductUIModel(
                productId = productId,
                productName = "Name 2",
                stock = "0",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            )
        )

        viewModel.setStockAvailability(sellableProductList)
        viewModel.setVariantStock(productId, 1)

        assert(viewModel.shouldDisplayVariantStockWarningLiveData.value == false)
    }

    @Test
    fun `given one of product stock is empty when setVariantStock should hide variant stock warning and show stock info`() {
        val sellableProductList = listOf(
            SellableStockProductUIModel(
                productId = "0",
                productName = "Name 1",
                stock = "1",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            ),
            SellableStockProductUIModel(
                productId = "1",
                productName = "Name 2",
                stock = "1",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            )
        )

        viewModel.setStockAvailability(sellableProductList)
        viewModel.setVariantStock("1", 1)
        viewModel.setVariantStock("0", 0)

        verifyHideVariantStockWarning()
        verifyShowStockInfo()
    }

    @Test
    fun `given all of product stock is empty when setVariantStock should show variant stock warning and hide stock info`() {
        val sellableProductList = listOf(
            SellableStockProductUIModel(
                productId = "0",
                productName = "Name 1",
                stock = "1",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            ),
            SellableStockProductUIModel(
                productId = "1",
                productName = "Name 2",
                stock = "1",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            )
        )

        viewModel.setStockAvailability(sellableProductList)
        viewModel.setVariantStock("1", 0)
        viewModel.setVariantStock("0", 0)

        verifyShowVariantStockWarning()
        verifyHideStockInfo()
    }

    @Test
    fun `given stock not valid when setStockAvailability should set variant stock empty`() {
        val sellableProductList = listOf(
            SellableStockProductUIModel(
                productId = "0",
                productName = "Name 1",
                stock = "0",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            ),
            SellableStockProductUIModel(
                productId = "1",
                productName = "Name 2",
                stock = "not valid stock",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            )
        )

        viewModel.setStockAvailability(sellableProductList)

        verifyShowVariantStockWarning()
        verifyHideStockInfo()
    }

    @Test
    fun `given some products stock is higher than max stock should set save button to disabled`() {
        val sellableProductList = listOf(
            SellableStockProductUIModel(
                productId = "0",
                productName = "Name 1",
                stock = "0",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            ),
            SellableStockProductUIModel(
                productId = "1",
                productName = "Name 2",
                stock = "3",
                maxStock = 2,
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            )
        )

        viewModel.setStockAvailability(sellableProductList)

        assert(viewModel.shouldEnableSaveButton.value == false)
    }

    @Test
    fun `given all products stock is lower than max stock should set save button to enabled`() {
        val sellableProductList = listOf(
            SellableStockProductUIModel(
                productId = "0",
                productName = "Name 1",
                stock = "0",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            ),
            SellableStockProductUIModel(
                productId = "1",
                productName = "Name 2",
                stock = "3",
                maxStock = 5,
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            )
        )

        viewModel.setStockAvailability(sellableProductList)

        assert(viewModel.shouldEnableSaveButton.value == true)
    }

    @Test
    fun `setting particular variant product stock more than once will also change the warning show condition value if condition were met`() {
        val productId = "12345"

        val sellableProductList = listOf(
            SellableStockProductUIModel(
                productId = productId,
                productName = "Name 2",
                stock = "0",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false,
                campaignTypeList = listOf()
            )
        )

        viewModel.setStockAvailability(sellableProductList)
        viewModel.setVariantStock(productId, 0)

        val currentWarningShowCondition = viewModel.shouldDisplayVariantStockWarningLiveData.value

        viewModel.setVariantStock(productId, 1)

        assert(viewModel.shouldDisplayVariantStockWarningLiveData.value != currentWarningShowCondition)
    }
}