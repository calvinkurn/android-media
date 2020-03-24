package com.tokopedia.product.manage.feature.list

import android.accounts.NetworkErrorException
import com.tokopedia.product.manage.data.createProduct
import com.tokopedia.product.manage.data.createProductViewModel
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.product.manage.feature.list.view.model.SetFeaturedProductResult
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Data
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.oldlist.data.model.featuredproductresponse.FeaturedProductResponseModel
import com.tokopedia.product.manage.oldlist.data.model.featuredproductresponse.GoldManageFeaturedProductV2
import com.tokopedia.product.manage.oldlist.data.model.featuredproductresponse.Header
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Picture
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Price
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductList
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductListData
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOrderOption
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

    @Test
    fun `when_setFilter_should_update_filter_accordingly`() {
        val selectedFilter = listOf(FilterOption.FilterByCondition.CashBackOnly, FilterOption.FilterByCondition.NewOnly)
        val filterOptionWrapper = FilterOptionWrapper(
                SortOption.SortByName(SortOrderOption.ASC),
                listOf(FilterOption.FilterByCondition.CashBackOnly, FilterOption.FilterByCondition.NewOnly),
                listOf(true, true, false, true))

        viewModel.setFilterOptionWrapper(filterOptionWrapper)
        viewModel.setSelectedFilter(selectedFilter)

        verifySelectedFiltersEquals(selectedFilter)
    }

    @Test
    fun `when_setFilterOptionWrapper_should_update_filterOptionWrapper_accordingly`() {
        val filterOptionWrapper = FilterOptionWrapper(
                SortOption.SortByName(SortOrderOption.ASC),
                listOf(FilterOption.FilterByCondition.CashBackOnly, FilterOption.FilterByCondition.NewOnly),
                listOf(true, true, false, true))

        viewModel.setFilterOptionWrapper(filterOptionWrapper)

        verifyFilterOptionWrapperEquals(filterOptionWrapper)
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

    @Test
    fun `get product list should map product to view model`() {
        runBlocking {
            val shopId = "1500"

            val price = "10000"
            val priceFormatted = "Rp10.000"
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(createProduct(name = "Tolak Angin Madu", price = Price(10000), pictures = pictures))
            val productListData = ProductListData(ProductList(header = null, data = productList))

            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId)

            val productViewModelList = listOf(createProductViewModel(
                name = "Tolak Angin Madu", price = price, priceFormatted = priceFormatted))
            val expectedProductList = Success(productViewModelList)

            verifyGetProductListCalled()
            verifyGetProductListResultEquals(expectedProductList)
        }
    }

    @Test
    fun `get product list should fail with exception`() {
        runBlocking {
            val exception = NullPointerException()

            onGetProductList_thenError(exception)

            viewModel.getProductList("1000")

            val expectedError = Fail(exception)

            verifyGetProductListCalled()
            verifyGetProductListErrorEquals(expectedError)
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

    private suspend fun onGetProductList_thenReturn(productListData: ProductListData) {
        coEvery { getProductListUseCase.execute(any()) } returns productListData
    }

    private suspend fun onGetProductList_thenError(error: Throwable) {
        coEvery { getProductListUseCase.execute(any()) } coAnswers { throw error }
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

    private fun verifyGetProductListCalled() {
        coVerify { getProductListUseCase.execute(any()) }
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

    private fun verifySetFeaturedProductResponseEquals(expectedResponse: Success<SetFeaturedProductResult>) {
        val actualSetFeaturedProductResult = viewModel.setFeaturedProductResult.value as Success<SetFeaturedProductResult>
        assertEquals(expectedResponse, actualSetFeaturedProductResult)
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

    private fun verifySelectedFiltersEquals(expectedSelectedFilters: List<FilterOption>) {
        val actualSelectedFilters = viewModel.selectedFilterAndSort.value?.filterOptions
        assertEquals(expectedSelectedFilters, actualSelectedFilters)
    }

    private fun verifyFilterOptionWrapperEquals(expectedFilterOptionWrapper: FilterOptionWrapper) {
        val actualFilterOptionWrapper = viewModel.selectedFilterAndSort.value
        assertEquals(expectedFilterOptionWrapper, actualFilterOptionWrapper)
    }

    private fun verifyGetProductListResultEquals(expectedResult: Success<List<ProductViewModel>>) {
        val actualResult = viewModel.productListResult.value
        assertEquals(expectedResult, actualResult)
    }

    private fun verifyGetProductListErrorEquals(expectedError: Fail) {
        val actualResult = viewModel.productListResult.value as Fail
        assertEquals(expectedError.throwable::class.java, actualResult.throwable::class.java)
    }
}