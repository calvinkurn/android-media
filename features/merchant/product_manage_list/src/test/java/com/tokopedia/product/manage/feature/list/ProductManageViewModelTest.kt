package com.tokopedia.product.manage.feature.list

import android.accounts.NetworkErrorException
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Data
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ProductManageViewModelTest: ProductManageViewModelTestFixture() {

    @Test
    fun `when_editPrice_success__should_return_edit_price_success_result`() {
        runBlocking {
            val productId = "0"
            val price = "10000"
            val productName = "Amazing Product"
            val productUpdateV3Response = ProductUpdateV3Response(productUpdateV3Data =
                    ProductUpdateV3Data(isSuccess = true)
            )

            onEditPrice_thenReturn(productUpdateV3Response)

            viewModel.editPrice(productId, price, productName)

            val expectedEditPriceResult = Success(EditPriceResult(productName, productId, price))

            verifyEditPriceUseCaseCalled()
            verifyEditPriceResponseSuccess(expectedEditPriceResult)
        }
    }

    @Test
    fun `when_editStock_success__should_return_edit_stock_success_result`() {
        runBlocking {
            val productId = "0"
            val stock = 0
            val productName = "Amazing Product"
            val status = ProductStatus.ACTIVE
            val productUpdateV3Response = ProductUpdateV3Response(productUpdateV3Data =
                ProductUpdateV3Data(isSuccess = true)
            )

            onEditStock_thenReturn(productUpdateV3Response)

            viewModel.editStock(productId, stock, productName, status)

            val expectedEditStockResult = Success(EditStockResult(productName, productId, stock, status))

            verifyEditStockUseCaseCalled()
            verifyEditStockResponseSuccess(expectedEditStockResult)
        }
    }

    @Test
    fun `when_deleteProduct_success__should_return_delete_product_success_result`() {
        runBlocking {
            val productId = "0"
            val productName = "Amazing Product"
            val productUpdateV3Response = ProductUpdateV3Response(productUpdateV3Data =
                ProductUpdateV3Data(isSuccess = true)
            )

            onDeleteProduct_thenReturn(productUpdateV3Response)

            viewModel.deleteSingleProduct(productName, productId)

            val expectedDeleteProductResult = Success(DeleteProductResult(productName, productId))

            verifyDeleteProductUseCaseCalled()
            verifyDeleteProductResponseSuccess(expectedDeleteProductResult)
        }
    }

    @Test
    fun `when_editPrice_fail__should_return_edit_price_fail_result`() {
        runBlocking {
            val productId = "0"
            val price = "10000"
            val productName = "Amazing Product"
            val productUpdateV3Response = ProductUpdateV3Response(productUpdateV3Data =
                ProductUpdateV3Data(isSuccess = false)
            )

            onEditPrice_thenReturn(productUpdateV3Response)

            viewModel.editPrice(productId, price, productName)

            val error = NetworkErrorException()
            val expectedEditPriceResult = Fail(EditPriceResult(productName, productId, price, error))

            verifyEditPriceUseCaseCalled()
            verifyEditPriceResponseFail(expectedEditPriceResult, error)
        }
    }

    @Test
    fun `when_editStock_fail__should_return_edit_stock_fail_result`() {
        runBlocking {
            val productId = "0"
            val stock = 0
            val productName = "Amazing Product"
            val status = ProductStatus.ACTIVE
            val productUpdateV3Response = ProductUpdateV3Response(productUpdateV3Data =
                ProductUpdateV3Data(isSuccess = false)
            )

            onEditStock_thenReturn(productUpdateV3Response)

            viewModel.editStock(productId, stock, productName, status)

            val error = NetworkErrorException()
            val expectedEditStockResult = Fail(EditStockResult(productName, productId, stock, status, error))

            verifyEditStockUseCaseCalled()
            verifyEditStockResponseFail(expectedEditStockResult, error)
        }
    }

    @Test
    fun `when_deleteProduct_fail__should_return_delete_product_fail_result`() {
        runBlocking {
            val productId = "0"
            val productName = "Amazing Product"
            val productUpdateV3Response = ProductUpdateV3Response(productUpdateV3Data =
                ProductUpdateV3Data(isSuccess = false)
            )

            onDeleteProduct_thenReturn(productUpdateV3Response)

            viewModel.deleteSingleProduct(productName, productId)

            val error = NetworkErrorException()
            val expectedDeleteProductResult = Fail(DeleteProductResult(productName, productId, error))

            verifyDeleteProductUseCaseCalled()
            verifyDeleteProductResponseFail(expectedDeleteProductResult, error)
        }
    }

    private suspend fun onEditPrice_thenReturn(productUpdateV3Response: ProductUpdateV3Response) {
        coEvery { editPriceUseCase.executeOnBackground() } returns productUpdateV3Response
    }

    private suspend fun onEditStock_thenReturn(productUpdateV3Response: ProductUpdateV3Response) {
        coEvery { editStockUseCase.executeOnBackground() } returns productUpdateV3Response
    }

    private suspend fun onDeleteProduct_thenReturn(productUpdateV3Response: ProductUpdateV3Response) {
        coEvery { deleteProductUseCase.executeOnBackground() } returns productUpdateV3Response
    }

    private fun verifyEditPriceUseCaseCalled() {
        coVerify { editPriceUseCase.executeOnBackground() }
    }

    private fun verifyEditStockUseCaseCalled() {
        coVerify { editStockUseCase.executeOnBackground() }
    }

    private fun verifyDeleteProductUseCaseCalled() {
        coVerify { deleteProductUseCase.executeOnBackground() }
    }

    private fun verifyEditPriceResponseSuccess(expectedResponse: Success<EditPriceResult>) {
        val actualEditPriceResult = (viewModel.editPriceResult.value as Success<EditPriceResult>)
        assertEquals(expectedResponse, actualEditPriceResult)
    }

    private fun verifyEditStockResponseSuccess(expectedResponse: Success<EditStockResult>) {
        val actualEditStockResult = (viewModel.editStockResult.value as Success<EditStockResult>)
        assertEquals(expectedResponse, actualEditStockResult)
    }

    private fun verifyDeleteProductResponseSuccess(expectedResponse: Success<DeleteProductResult>) {
        val actualDeleteStockResult = (viewModel.deleteProductResult.value as Success<DeleteProductResult>)
        assertEquals(expectedResponse, actualDeleteStockResult)
    }

    private fun verifyEditPriceResponseFail(expectedResponse: Fail, networkErrorException: NetworkErrorException) {
        var actualEditPriceResult = (viewModel.editPriceResult.value as Fail).throwable
        actualEditPriceResult = (actualEditPriceResult as EditPriceResult).copy(error = networkErrorException)
        assertEquals(expectedResponse.throwable as EditPriceResult, actualEditPriceResult)
    }

    private fun verifyEditStockResponseFail(expectedResponse: Fail, networkErrorException: NetworkErrorException) {
        var actualEditStockResult = (viewModel.editStockResult.value as Fail).throwable
        actualEditStockResult = (actualEditStockResult as EditStockResult).copy(error = networkErrorException)
        assertEquals(expectedResponse.throwable, actualEditStockResult)
    }

    private fun verifyDeleteProductResponseFail(expectedResponse: Fail, networkErrorException: NetworkErrorException) {
        var actualDeleteStockResult = (viewModel.deleteProductResult.value as Fail).throwable
        actualDeleteStockResult = (actualDeleteStockResult as DeleteProductResult).copy(error = networkErrorException)
        assertEquals(expectedResponse.throwable, actualDeleteStockResult)
    }
}