package com.tokopedia.product.manage.feature.campaignstock

import com.tokopedia.product.manage.data.createShopOwnerAccess
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

class CampaignMainStockViewModelTest: CampaignMainStockViewModelTestFixture() {

    @Test
    fun `setting variant stock should set the warning show conditions`() {
        viewModel.setVariantStock(anyString(), anyInt())

        assert(viewModel.shouldDisplayVariantStockWarningLiveData.value != null)
    }

    @Test
    fun `setting variant product with empty stock will set the warning to be displayed`() {
        viewModel.setVariantStock(anyString(), 0)

        assert(viewModel.shouldDisplayVariantStockWarningLiveData.value == true)
    }

    @Test
    fun `setting variant product with non-empty stock will set the warning to be not displayed`() {
        viewModel.setVariantStock(anyString(), 1)

        assert(viewModel.shouldDisplayVariantStockWarningLiveData.value == false)
    }

    @Test
    fun `when set one of product with empty stock should set displayVariantStockWarning false`() {
        val sellableProductList = listOf(
            SellableStockProductUIModel(
                productId = "0",
                productName = "Name 1",
                stock = "0",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess()
            ),
            SellableStockProductUIModel(
                productId = "1",
                productName = "Name 2",
                stock = "0",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess()
            )
        )

        viewModel.setStockAvailability(sellableProductList)
        viewModel.setVariantStock("1", 1)
        viewModel.setVariantStock("0", 0)

        val expectedShowWarning = false
        val actualShowWarning = viewModel.shouldDisplayVariantStockWarningLiveData.value

        assertEquals(expectedShowWarning, actualShowWarning)
    }

    @Test
    fun `when set all of product with empty stock should set displayVariantStockWarning true`() {
        val sellableProductList = listOf(
            SellableStockProductUIModel(
                productId = "0",
                productName = "Name 1",
                stock = "1",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess()
            ),
            SellableStockProductUIModel(
                productId = "1",
                productName = "Name 2",
                stock = "1",
                isActive = true,
                isAllStockEmpty = false,
                access = createShopOwnerAccess()
            )
        )

        viewModel.setStockAvailability(sellableProductList)
        viewModel.setVariantStock("1", 0)
        viewModel.setVariantStock("0", 0)

        val expectedShowWarning = true
        val actualShowWarning = viewModel.shouldDisplayVariantStockWarningLiveData.value

        assertEquals(expectedShowWarning, actualShowWarning)
    }

    @Test
    fun `setting particular variant product stock more than once will also change the warning show condition value if condition were met`() {
        val productId = "12345"

        viewModel.setVariantStock(productId, 0)

        val currentWarningShowCondition = viewModel.shouldDisplayVariantStockWarningLiveData.value

        viewModel.setVariantStock(productId, 1)

        assert(viewModel.shouldDisplayVariantStockWarningLiveData.value != currentWarningShowCondition)
    }
}