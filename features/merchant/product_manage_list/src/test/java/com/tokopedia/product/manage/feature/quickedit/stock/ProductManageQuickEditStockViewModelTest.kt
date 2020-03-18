package com.tokopedia.product.manage.feature.quickedit.stock

import com.tokopedia.product.manage.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import junit.framework.Assert.assertEquals
import org.junit.Test

class ProductManageQuickEditStockViewModelTest: ProductManageQuickEditStockViewModelTestFixture() {

    @Test
    fun `when_stock_too_low_should_set_status_to_inactive_and_set_stock_to_minimum_value`() {
        val veryLowStock = -1

        viewModel.updateStock(veryLowStock)

        val expectedStatus = ProductStatus.INACTIVE
        val expectedStock = ProductManageQuickEditStockFragment.MINIMUM_STOCK

        verifyStockEquals(expectedStock)
        verifyStatusEquals(expectedStatus)
    }

    @Test
    fun `when_stock_too_high_should_set_status_to_active_and_set_stock_to_maximum_value`() {
        val veryHighStock = 1000000

        viewModel.updateStock(veryHighStock)

        val expectedStatus = ProductStatus.ACTIVE
        val expectedStock = ProductManageQuickEditStockFragment.MAXIMUM_STOCK

        verifyStockEquals(expectedStock)
        verifyStatusEquals(expectedStatus)
    }

    @Test
    fun `when_stock_is_valid_should_set_status_to_active_and set_stock_to_desired_value`() {
        val validStock = 10

        viewModel.updateStock(validStock)

        val expectedStatus = ProductStatus.ACTIVE

        verifyStockEquals(validStock)
        verifyStatusEquals(expectedStatus)
    }

    @Test
    fun `when_status_is_valid_should_set_status_to_desired_value`() {
        val validStatus = ProductStatus.ACTIVE

        viewModel.updateStatus(validStatus)

        verifyStatusEquals(validStatus)
    }

    private fun verifyStockEquals(expectedStock: Int) {
        val actualStock = viewModel.stock.value
        assertEquals(expectedStock, actualStock)
    }

    private fun verifyStatusEquals(expectedStatus: ProductStatus) {
        val actualStatus = viewModel.status.value
        assertEquals(expectedStatus, actualStatus)
    }
}