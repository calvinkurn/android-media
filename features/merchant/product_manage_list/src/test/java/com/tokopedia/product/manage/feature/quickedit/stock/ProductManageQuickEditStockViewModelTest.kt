package com.tokopedia.product.manage.feature.quickedit.stock

import com.tokopedia.product.manage.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import junit.framework.Assert.assertEquals
import org.junit.Test

class ProductManageQuickEditStockViewModelTest: ProductManageQuickEditStockViewModelTestFixture() {

    @Test
    fun `when stock too low should set status to inactive and set stock to minimum value`() {
        val veryLowStock = -1

        viewModel.updateStock(veryLowStock)

        val expectedStatus = ProductStatus.INACTIVE
        val expectedStock = ProductManageQuickEditStockFragment.MINIMUM_STOCK

        verifyStockEquals(expectedStock)
        verifyStatusEquals(expectedStatus)
    }

    @Test
    fun `when stock too high should set status to active and set stock to maximum value`() {
        val veryHighStock = 1000000

        viewModel.updateStock(veryHighStock)

        val expectedStatus = ProductStatus.ACTIVE
        val expectedStock = ProductManageQuickEditStockFragment.MAXIMUM_STOCK

        verifyStockEquals(expectedStock)
        verifyStatusEquals(expectedStatus)
    }

    @Test
    fun `when stock is valid should set stock to desired value`() {
        val validStock = 10
        val expectedStatus = ProductStatus.ACTIVE

        viewModel.updateStock(validStock)

        verifyStockEquals(validStock)
        verifyStatusEquals(expectedStatus)
    }

    @Test
    fun `when status is valid should set status to desired value`() {
        val validStatus = ProductStatus.ACTIVE

        viewModel.updateStatus(validStatus)

        verifyStatusEquals(validStatus)
    }

    @Test
    fun `when update sotck is submit should set stock to desired value and keep status the same`() {
        val validStatus = ProductStatus.INACTIVE
        val validStock = 10

        viewModel.updateStatus(validStatus)
        viewModel.updateStock(validStock, true)

        verifyStatusEquals(validStatus)
        verifyStockEquals(validStock)
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