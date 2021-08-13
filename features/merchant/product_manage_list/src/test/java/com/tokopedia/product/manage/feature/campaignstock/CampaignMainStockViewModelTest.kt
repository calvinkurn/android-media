package com.tokopedia.product.manage.feature.campaignstock

import com.tokopedia.product.manage.data.createShopOwnerAccess
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import org.junit.Test

class CampaignMainStockViewModelTest: CampaignMainStockViewModelTestFixture() {

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
                isCampaign = false
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
                isCampaign = false
            ),
            SellableStockProductUIModel(
                productId = "1",
                productName = "Name 2",
                stock = "1",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false
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
                isCampaign = false
            ),
            SellableStockProductUIModel(
                productId = "1",
                productName = "Name 2",
                stock = "1",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false
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
                isCampaign = false
            ),
            SellableStockProductUIModel(
                productId = "1",
                productName = "Name 2",
                stock = "not valid stock",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess(),
                isCampaign = false
            )
        )

        viewModel.setStockAvailability(sellableProductList)

        verifyShowVariantStockWarning()
        verifyHideStockInfo()
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
                isCampaign = false
            )
        )

        viewModel.setStockAvailability(sellableProductList)
        viewModel.setVariantStock(productId, 0)

        val currentWarningShowCondition = viewModel.shouldDisplayVariantStockWarningLiveData.value

        viewModel.setVariantStock(productId, 1)

        assert(viewModel.shouldDisplayVariantStockWarningLiveData.value != currentWarningShowCondition)
    }
}