package com.tokopedia.product.manage.feature.quickedit.stock

import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_STOCK
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MINIMUM_STOCK
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ProductManageQuickEditStockViewModelTest: ProductManageQuickEditStockViewModelTestFixture() {

    @Test
    fun `when stock too low should set stock to minimum value`() {
        val veryLowStock = -1

        viewModel.updateStock(veryLowStock)

        val expectedStock = MINIMUM_STOCK

        verifyStockEquals(expectedStock)
    }

    @Test
    fun `when stock too high should set stock to maximum value`() {
        val veryHighStock = 1000000

        viewModel.updateStock(veryHighStock)

        val expectedStock = MAXIMUM_STOCK

        verifyStockEquals(expectedStock)
    }

    @Test
    fun `when stock is valid should set stock to desired value`() {
        val validStock = 10

        viewModel.updateStock(validStock)

        verifyStockEquals(validStock)
    }

    @Test
    fun `when status is valid should set status to desired value`() {
        val validStatus = ProductStatus.ACTIVE

        viewModel.updateStatus(validStatus)

        verifyStatusEquals(validStatus)
    }

    @Test
    fun `when update stock is submit should set stock to desired value and keep status the same`() {
        val validStatus = ProductStatus.INACTIVE
        val validStock = 12

        viewModel.updateStatus(validStatus)
        viewModel.updateStock(validStock)

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