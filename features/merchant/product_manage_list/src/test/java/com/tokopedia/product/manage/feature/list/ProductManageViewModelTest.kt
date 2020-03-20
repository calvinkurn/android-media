package com.tokopedia.product.manage.feature.list

import com.tokopedia.product.manage.feature.list.view.model.SetFeaturedProductResult
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Data
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.oldlist.data.model.featuredproductresponse.FeaturedProductResponseModel
import com.tokopedia.product.manage.oldlist.data.model.featuredproductresponse.GoldManageFeaturedProductV2
import com.tokopedia.product.manage.oldlist.data.model.featuredproductresponse.Header
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ProductManageViewModelTest: ProductManageViewModelTestFixture() {

    @Test
    fun `editPrice should execute expected use case`() {
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
            verifyEditPriceResponseEquals(expectedEditPriceResult)
        }
    }

    @Test
    fun `editStock should execute expected use case`() {
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
            verifyEditStockResponseEquals(expectedEditStockResult)
        }
    }

    @Test
    fun `deleteProduct should execute expected use case`() {
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
            verifyDeleteProductResponseEquals(expectedDeleteProductResult)
        }
    }

    @Test
    fun `setFeaturedProduct should execute expected use case`() {
        runBlocking {
            val productId = "123"
            val status = 1
            val errorCode = "none"
            val message = listOf("test", "test")
            val header = Header(errorCode, message)
            val goldManageFeaturedProductV2 = GoldManageFeaturedProductV2(header)
            val featuredProductResponseModel = FeaturedProductResponseModel(goldManageFeaturedProductV2)

            onSetFeaturedProduct_thenReturn(featuredProductResponseModel)

            viewModel.setFeaturedProduct(productId, status)

            val expectedFeaturedProductResult = Success(SetFeaturedProductResult(productId, status))

            verifySetFeaturedProductUseCaseCalled()
            verifySetFeaturedProductResponseEquals(expectedFeaturedProductResult)
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

    private suspend fun onSetFeaturedProduct_thenReturn(featuredProductResponseModel: FeaturedProductResponseModel) {
        coEvery { setFeaturedProductUseCase.executeOnBackground() } returns featuredProductResponseModel
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

    private fun verifySetFeaturedProductUseCaseCalled() {
        coVerify { setFeaturedProductUseCase.executeOnBackground() }
    }

    private fun verifyEditPriceResponseEquals(expectedResponse: Success<EditPriceResult>) {
        val actualEditPriceResult = (viewModel.editPriceResult.value as Success<EditPriceResult>)
        assertEquals(expectedResponse, actualEditPriceResult)
    }

    private fun verifyEditStockResponseEquals(expectedResponse: Success<EditStockResult>) {
        val actualEditStockResult = (viewModel.editStockResult.value as Success<EditStockResult>)
        assertEquals(expectedResponse, actualEditStockResult)
    }

    private fun verifyDeleteProductResponseEquals(expectedResponse: Success<DeleteProductResult>) {
        val actualDeleteStockResult = (viewModel.deleteProductResult.value as Success<DeleteProductResult>)
        assertEquals(expectedResponse, actualDeleteStockResult)
    }

    private fun verifySetFeaturedProductResponseEquals(expectedResponse: Success<SetFeaturedProductResult>) {
        val actualSetFeaturedProductResult = viewModel.setFeaturedProductResult.value as Success<SetFeaturedProductResult>
        assertEquals(expectedResponse, actualSetFeaturedProductResult)
    }
}