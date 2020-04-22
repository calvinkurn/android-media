package com.tokopedia.product.manage.feature.list

import android.accounts.NetworkErrorException
import com.tokopedia.product.manage.data.createProduct
import com.tokopedia.product.manage.data.createProductViewModel
import com.tokopedia.product.manage.feature.list.view.model.SetFeaturedProductResult
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaData
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaResponse
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaWrapper
import com.tokopedia.product.manage.feature.filter.data.model.Tab
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.*
import com.tokopedia.product.manage.feature.list.view.model.ShopInfoResult
import com.tokopedia.product.manage.feature.multiedit.data.response.MultiEditProduct
import com.tokopedia.product.manage.feature.multiedit.data.response.MultiEditProductResult
import com.tokopedia.product.manage.feature.multiedit.data.response.MultiEditProductResult.*
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Data
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.oldlist.data.model.featuredproductresponse.FeaturedProductResponseModel
import com.tokopedia.product.manage.oldlist.data.model.featuredproductresponse.GoldManageFeaturedProductV2
import com.tokopedia.product.manage.oldlist.data.model.featuredproductresponse.Header
import com.tokopedia.product.manage.verification.verifyErrorEquals
import com.tokopedia.product.manage.verification.verifySuccessEquals
import com.tokopedia.product.manage.verification.verifyValueEquals
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Picture
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Price
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductList
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductListData
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterByCondition.*
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOption.*
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOrderOption.*
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCore
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ProductManageViewModelTest: ProductManageViewModelTestFixture() {

    @Test
    fun `when editPrice success should return_edit price success result`() {
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
            viewModel.editPriceResult.verifySuccessEquals(expectedEditPriceResult)
        }
    }

    @Test
    fun `when editStock success should return edit stock success result`() {
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
            viewModel.editStockResult.verifySuccessEquals(expectedEditStockResult)
        }
    }

    @Test
    fun `when deleteProduct success should return delete product success result`() {
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
            viewModel.deleteProductResult.verifySuccessEquals(expectedDeleteProductResult)
        }
    }

    @Test
    fun `when editPrice fail should return edit price fail result`() {
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
            viewModel.editPriceResult.verifyErrorEquals(expectedEditPriceResult)
        }
    }

    @Test
    fun `when editStock fail should return edit stock fail result`() {
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
            viewModel.editStockResult.verifyErrorEquals(expectedEditStockResult)
        }
    }

    @Test
    fun `when deleteProduct fail should return delete product fail result`() {
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
            viewModel.deleteProductResult.verifyErrorEquals(expectedDeleteProductResult)
        }
    }

    @Test
    fun `when setFilter with no filter option wrapper should update filter accordingly`() {
        val selectedFilter = listOf(CashBackOnly, NewOnly)

        viewModel.setSelectedFilter(selectedFilter)

        verifySelectedFiltersEquals(selectedFilter)
    }

    @Test
    fun `when setFilter should update filter accordingly`() {
        val filterOptionWrapper = FilterOptionWrapper(
                SortByName(ASC),
                listOf(CashBackOnly, NewOnly),
                listOf(true, true, false, true))
        val selectedFilter = listOf(CashBackOnly, NewOnly)

        viewModel.setFilterOptionWrapper(filterOptionWrapper)
        viewModel.setSelectedFilter(selectedFilter)

        verifySelectedFiltersEquals(selectedFilter)
    }

    @Test
    fun `when setFilterOptionWrapper should update filterOptionWrapper accordingly`() {
        val filterOptionWrapper = FilterOptionWrapper(
                SortByName(ASC),
                listOf(CashBackOnly, NewOnly),
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

            viewModel.productListResult
                .verifySuccessEquals(expectedProductList)
        }
    }

    @Test
    fun `get product list should fail with exception`() {
        runBlocking {
            val exception = NullPointerException()

            onGetProductList_thenError(exception)

            viewModel.getProductList("1000")

            val expectedError = Fail(exception)

            viewModel.productListResult
                .verifyErrorEquals(expectedError)
        }
    }

    @Test
    fun `get filters tab should map response to filter tab result`() {
        val tabs = listOf(Tab(id = "ACTIVE", name = "Active", value = "10"))
        val productListMetaData = ProductListMetaData(tabs = tabs)
        val productListMeta = ProductListMetaWrapper(productListMetaData = productListMetaData)
        val productListMetaResponse = ProductListMetaResponse(productListMeta)

        val filterList = listOf(CashBackOnly, FeaturedOnly)
        val filterOptions = FilterOptionWrapper(filterOptions = filterList, sortOption = SortByName(ASC), selectedFilterCount = 3)

        onGetFiltersTab_thenReturn(productListMetaResponse)

        viewModel.setFilterOptionWrapper(filterOptions)
        viewModel.getFiltersTab()

        val filterTabList = listOf(MoreFilter(3),Active(10))
        val expectedResult = Success(GetFilterTabResult(filterTabList, 10))

        viewModel.productFiltersTab
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `get filters tab should fail with exception`() {
        runBlocking {
            val exception = NullPointerException()

            onGetFiltersTab_thenError(exception)

            viewModel.getFiltersTab()

            val expectedError = Fail(exception)

            viewModel.productFiltersTab
                .verifyErrorEquals(expectedError)
        }
    }

    @Test
    fun `get gold merchant status should set shop info result success`() {
        runBlocking {
            val isGoldMerchant = true
            val isOfficialStore = true
            val shopDomain = "http://www.tokopedia.com/#1"
            val shopCore = ShopCore(domain = shopDomain)
            val goldOS = ShopInfo.GoldOS(isGold = 1, isOfficial = 1)
            val shopInfoResponse = ShopInfo(shopCore = shopCore, goldOS = goldOS)

            onGetShopInfo_thenReturn(shopInfoResponse)

            viewModel.getGoldMerchantStatus()

            val expectedResult = Success(ShopInfoResult(shopDomain, isGoldMerchant, isOfficialStore))

            viewModel.shopInfoResult
                .verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `get gold merchant status should fail with exception`() {
        runBlocking {
            val exception = NullPointerException()

            onGetShopInfo_thenError(exception)

            viewModel.getGoldMerchantStatus()

            val expectedError = Fail(exception)

            viewModel.shopInfoResult
                .verifyErrorEquals(expectedError)
        }
    }

    @Test
    fun `edit multiple products by status should return success and failed response`() {
        val status = ProductStatus.ACTIVE
        val successResponse = MultiEditProductResult(productID = "1", result = Result(isSuccess = true))
        val failedResponse =  MultiEditProductResult(productID = "2", result = Result(isSuccess = false))
        val response = MultiEditProduct(listOf(successResponse, failedResponse))

        onMultiEditProducts_thenReturn(response)

        viewModel.editProductsByStatus(listOf("1", "2"), status)

        val expectedResult = Success(EditByStatus(status, listOf(successResponse), listOf(failedResponse)))

        viewModel.multiEditProductResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `edit multiple products by status should fail with exception`() {
        val status = ProductStatus.ACTIVE
        val exception = NullPointerException()

        onMultiEditProducts_thenError(exception)

        viewModel.editProductsByStatus(listOf("1", "2"), status)

        val expectedError = Fail(exception)

        viewModel.multiEditProductResult
            .verifyErrorEquals(expectedError)
    }

    @Test
    fun `edit multiple products by etalase should return success and failed response`() {
        val menuId = "1"
        val menuName = "Etalase Toko"
        val successResponse = MultiEditProductResult(productID = "1", result = Result(isSuccess = true))
        val failedResponse =  MultiEditProductResult(productID = "2", result = Result(isSuccess = false))
        val response = MultiEditProduct(listOf(successResponse, failedResponse))

        onMultiEditProducts_thenReturn(response)

        viewModel.editProductsEtalase(listOf("1", "2"), menuId, menuName)

        val expectedResult = Success(EditByMenu(menuId, menuName, listOf(successResponse), listOf(failedResponse)))

        viewModel.multiEditProductResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `edit multiple products by etalase should fail with exception`() {
        val menuId = "1"
        val menuName = "Etalase Toko"
        val exception = NullPointerException()

        onMultiEditProducts_thenError(exception)

        viewModel.editProductsEtalase(listOf("1", "2"), menuId, menuName)

        val expectedError = Fail(exception)

        viewModel.multiEditProductResult
            .verifyErrorEquals(expectedError)
    }

    @Test
    fun `get total product count should return total product count from filters tab`() {
        val tabs = listOf(Tab(id = "ACTIVE", name = "Active", value = "10"))
        val productListMetaData = ProductListMetaData(tabs = tabs)
        val productListMeta = ProductListMetaWrapper(productListMetaData = productListMetaData)
        val productListMetaResponse = ProductListMetaResponse(productListMeta)

        onGetFiltersTab_thenReturn(productListMetaResponse)

        viewModel.getFiltersTab()

        val expectedProductCount = 10
        val actualProductCount = viewModel.getTotalProductCount()

        assertEquals(expectedProductCount, actualProductCount)
    }

    @Test
    fun `toggle multi select should switch multi select enabled state`() {
        viewModel.toggleMultiSelect()

        viewModel.toggleMultiSelect
            .verifyValueEquals(true)

        viewModel.toggleMultiSelect()

        viewModel.toggleMultiSelect
            .verifyValueEquals(false)
    }

    @Test
    fun `get featured product count should excecute expected usecase`() {
        runBlocking {
            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(createProduct(name = "Tolak Angin Madu", price = Price(10000), pictures = pictures))
            val productListData = ProductListData(ProductList(header = null, data = productList))

            onGetProductList_thenReturn(productListData)

            viewModel.getFeaturedProductCount(shopId)

            val expectedFeaturedProductCount = Success(1)

            verifyGetProductListCalled()
            viewModel.productListFeaturedOnlyResult.verifySuccessEquals(expectedFeaturedProductCount)
        }
    }

    @Test
    fun `when isPowerMerchant should return power merchant status`() {
        val actualIsPowerMerchant= viewModel.isPowerMerchant()
        val expectedIsPowerMerchant = false
        assertEquals(expectedIsPowerMerchant, actualIsPowerMerchant)
    }

    @Test
    fun `when reset selected filter should set filter to initial state`() {
        viewModel.resetSelectedFilter()

        val expectedFilter = FilterOptionWrapper(
            filterShownState = listOf(true, true, false, false))

        viewModel.selectedFilterAndSort
            .verifyValueEquals(expectedFilter)
    }

    private fun onMultiEditProducts_thenError(exception: NullPointerException) {
        coEvery { multiEditProductUseCase.execute(any()) } coAnswers { throw exception }
    }

    private fun onMultiEditProducts_thenReturn(response: MultiEditProduct) {
        coEvery { multiEditProductUseCase.execute(any()) } returns response
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

    private fun onGetFiltersTab_thenReturn(response: ProductListMetaResponse) {
        coEvery { getProductListMetaUseCase.executeOnBackground() } returns response
    }

    private fun onGetFiltersTab_thenError(error: Throwable) {
        coEvery { getProductListMetaUseCase.executeOnBackground() } coAnswers { throw error }
    }

    private fun onGetShopInfo_thenReturn(shopInfoResponse: ShopInfo) {
        coEvery { gqlGetShopInfoUseCase.executeOnBackground() } returns shopInfoResponse
    }

    private fun onGetShopInfo_thenError(error: Throwable) {
        coEvery { gqlGetShopInfoUseCase.executeOnBackground() } coAnswers { throw error }
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
        coVerify { getProductListUseCase.execute(any())}
    }

    private fun verifySetFeaturedProductResponseEquals(expectedResponse: Success<SetFeaturedProductResult>) {
        val actualSetFeaturedProductResult = viewModel.setFeaturedProductResult.value as Success<SetFeaturedProductResult>
        assertEquals(expectedResponse, actualSetFeaturedProductResult)
    }

    private fun verifySelectedFiltersEquals(expectedSelectedFilters: List<FilterOption>) {
        val actualSelectedFilters = viewModel.selectedFilterAndSort.value?.filterOptions
        assertEquals(expectedSelectedFilters, actualSelectedFilters)
    }

    private fun verifyFilterOptionWrapperEquals(expectedFilterOptionWrapper: FilterOptionWrapper) {
        val actualFilterOptionWrapper = viewModel.selectedFilterAndSort.value
        assertEquals(expectedFilterOptionWrapper, actualFilterOptionWrapper)
    }
}