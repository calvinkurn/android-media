package com.tokopedia.product.manage.feature.campaignstock

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
    fun `adding non empty stock variant product with empty stock product will also set the warning to be displayed`() {
        viewModel.setVariantStock("1", 1)
        viewModel.setVariantStock("0", 0)

        assert(viewModel.shouldDisplayVariantStockWarningLiveData.value == true)
    }

    @Test
    fun `adding empty stock variant product with non-empty stock product will not change the warning show condition`() {
        viewModel.setVariantStock("0", 0)

        val currentWarningShowCondition = viewModel.shouldDisplayVariantStockWarningLiveData.value

        viewModel.setVariantStock("1", 1)

        assert(viewModel.shouldDisplayVariantStockWarningLiveData.value == currentWarningShowCondition)
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