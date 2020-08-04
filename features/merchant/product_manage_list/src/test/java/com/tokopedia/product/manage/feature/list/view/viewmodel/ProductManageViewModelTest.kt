package com.tokopedia.product.manage.feature.list.view.viewmodel

import android.accounts.NetworkErrorException
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.data.createEditVariantResult
import com.tokopedia.product.manage.data.createProduct
import com.tokopedia.product.manage.data.createProductViewModel
import com.tokopedia.product.manage.feature.filter.data.model.*
import com.tokopedia.product.manage.feature.list.data.model.FeaturedProductResponseModel
import com.tokopedia.product.manage.feature.list.data.model.GoldManageFeaturedProductV2
import com.tokopedia.product.manage.feature.list.data.model.Header
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel.Active
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult.ShowFilterTab
import com.tokopedia.product.manage.feature.list.view.model.GetPopUpResult
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByMenu
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByStatus
import com.tokopedia.product.manage.feature.list.view.model.PriceUiModel
import com.tokopedia.product.manage.feature.list.view.model.SetFeaturedProductResult
import com.tokopedia.product.manage.feature.list.view.model.ShopInfoResult
import com.tokopedia.product.manage.feature.list.view.model.TopAdsInfo
import com.tokopedia.product.manage.feature.list.view.model.ViewState.HideProgressDialog
import com.tokopedia.product.manage.feature.multiedit.data.response.MultiEditProduct
import com.tokopedia.product.manage.feature.multiedit.data.response.MultiEditProductResult
import com.tokopedia.product.manage.feature.multiedit.data.response.MultiEditProductResult.Result
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Data
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Header
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.verification.verifyErrorEquals
import com.tokopedia.product.manage.verification.verifySuccessEquals
import com.tokopedia.product.manage.verification.verifyValueEquals
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsCategory.AUTO_ADS
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsResponse
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsResponse.*
import com.tokopedia.shop.common.data.source.cloud.model.productlist.*
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterByCondition.*
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOption.SortByName
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOrderOption.ASC
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCore
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verifyAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import rx.Subscriber

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
    fun `given selected filter and sort null when setSelectedFilter should set default filter option`() {
        val selectedFilter = listOf(CashBackOnly, NewOnly)

        viewModel.setSelectedFilter(selectedFilter)

        val expectedSelectedFilter = FilterOptionWrapper(
            null,
            selectedFilter,
            listOf(true, true, false, false)
        )

        viewModel.selectedFilterAndSort
            .verifyValueEquals(expectedSelectedFilter)
    }

    @Test
    fun `given sort option null when setSelectedFilter should NOT add sort option to filter count`() {
        val sortOption = null
        val selectedFilterCount = 2
        val selectedFilter = listOf(CashBackOnly, NewOnly)

        val filterOption = FilterOptionWrapper(
            sortOption,
            selectedFilter,
            listOf(true, true, false, false),
            selectedFilterCount
        )

        viewModel.setFilterOptionWrapper(filterOption)
        viewModel.setSelectedFilter(selectedFilter)

        val expectedFilterOption = FilterOptionWrapper(
            null,
            selectedFilter,
            listOf(true, true, false, true),
            selectedFilterCount
        )

        viewModel.selectedFilterAndSort
            .verifyValueEquals(expectedFilterOption)
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

            val minPrice = PriceUiModel("10000", "Rp10.000")
            val maxPrice = PriceUiModel("100000", "Rp100.000")
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(createProduct(name = "Tolak Angin Madu", price = Price(10000, 100000), pictures = pictures))
            val productListData = ProductListData(ProductList(header = null, data = productList))

            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId)

            val topAdsInfo = TopAdsInfo(isTopAds = false, isAutoAds = false)
            val productViewModelList = listOf(createProductViewModel(
                name = "Tolak Angin Madu", minPrice = minPrice, maxPrice = maxPrice, topAds = topAdsInfo))
            val expectedProductList = Success(productViewModelList)

            viewModel.productListResult
                .verifySuccessEquals(expectedProductList)

            verifyHideProgressBar()
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

        val filterTabList = listOf(Active(10))
        val expectedResult = Success(ShowFilterTab(filterTabList, 10))

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
            val shopInfoTopAdsData = ShopInfoTopAds(Data(AUTO_ADS, "auto ads"))
            val shopInfoTopAdsResponse = ShopInfoTopAdsResponse(shopInfoTopAdsData)

            onGetShopInfo_thenReturn(shopInfoResponse)
            onGetShopInfoTopAds_thenReturn(shopInfoTopAdsResponse)

            viewModel.getGoldMerchantStatus()

            val data = ShopInfoResult(shopDomain, isGoldMerchant, isOfficialStore)
            val expectedResult = Success(data)

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
    fun `given get filters tab is null when get total product count should return zero`() {
        onGetFiltersTab_thenError(NullPointerException())

        viewModel.getFiltersTab()
        viewModel.getTotalProductCount()

        val expectedProductCount = 0
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

    @Test
    fun `when edit variant price success should set live data value success`() {
        val data = ProductUpdateV3Data(isSuccess = true)
        val response = ProductUpdateV3Response(data)
        val result = createEditVariantResult()

        onEditProductVariant_thenReturn(response)

        viewModel.editVariantsPrice(result)

        val expectedResult = Success(result)

        viewModel.editVariantPriceResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when edit variant price NOT success should set live data value fail`() {
        val errorMessage = "can't edit price :("
        val headerResponse = ProductUpdateV3Header(errorMessage = arrayListOf(errorMessage))
        val data = ProductUpdateV3Data(isSuccess = false, header = headerResponse)
        val response = ProductUpdateV3Response(data)
        val result = createEditVariantResult()

        onEditProductVariant_thenReturn(response)

        viewModel.editVariantsPrice(result)

        val expectedResult = Fail(MessageErrorException(errorMessage))

        viewModel.editVariantPriceResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when edit variant price error should set live data value fail`() {
        val error = NullPointerException()
        val result = createEditVariantResult()

        onEditProductVariant_thenReturn(error)

        viewModel.editVariantsPrice(result)

        val expectedResult = Fail(error)

        viewModel.editVariantPriceResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when edit variant stock success should set live data value success`() {
        val data = ProductUpdateV3Data(isSuccess = true)
        val response = ProductUpdateV3Response(data)
        val result = createEditVariantResult()

        onEditProductVariant_thenReturn(response)

        viewModel.editVariantsStock(result)

        val expectedResult = Success(result)

        viewModel.editVariantStockResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when edit variant stock NOT success should set live data value fail`() {
        val errorMessage = "can't edit stock :("
        val headerResponse = ProductUpdateV3Header(errorMessage = arrayListOf(errorMessage))
        val data = ProductUpdateV3Data(isSuccess = false, header = headerResponse)
        val response = ProductUpdateV3Response(data)
        val result = createEditVariantResult()

        onEditProductVariant_thenReturn(response)

        viewModel.editVariantsStock(result)

        val expectedResult = Fail(MessageErrorException(errorMessage))

        viewModel.editVariantStockResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when edit variant stock error should set live data value fail`() {
        val error = NullPointerException()
        val result = createEditVariantResult()

        onEditProductVariant_thenReturn(error)

        viewModel.editVariantsStock(result)

        val expectedResult = Fail(error)

        viewModel.editVariantStockResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when get free claim success should set live data value success`() {
        val dataDeposit = DataDeposit()

        onGetFreeClaim_thenReturn(dataDeposit)

        viewModel.getFreeClaim("query", "1")

        val expectedResult = Success(dataDeposit)

        viewModel.getFreeClaimResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get free claim error should set live data value fail`() {
        val error = NullPointerException()

        onGetFreeClaim_thenReturn(error)

        viewModel.getFreeClaim("query", "1")

        val expectedResult = Fail(error)

        viewModel.getFreeClaimResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when get popups info success should set live data value success`() {
        val productId = "1"
        val showPopup = true

        onGetPopupsInfo_thenReturn(showPopup)

        viewModel.getPopupsInfo(productId)

        val expectedResult = Success(GetPopUpResult(productId, showPopup))

        viewModel.getPopUpResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get popups info error should set live data value fail`() {
        val error = NullPointerException()

        onGetPopupsInfo_thenReturn(error)

        viewModel.getPopupsInfo("1")

        val expectedResult = Fail(error)

        viewModel.getPopUpResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when detach view should call use case unsubscribe and cancelJobs`() {
        viewModel.detachView()

        verifyAll {
            gqlGetShopInfoUseCase.cancelJobs()
            topAdsGetShopDepositGraphQLUseCase.unsubscribe()
            popupManagerAddProductUseCase.unsubscribe()
            getProductListUseCase.cancelJobs()
            setFeaturedProductUseCase.cancelJobs()
        }
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

    private fun onGetShopInfoTopAds_thenReturn(shopInfoTopAdsResponse: ShopInfoTopAdsResponse) {
        coEvery { geetShopInfoTopAdsUseCase.execute(any()) } returns shopInfoTopAdsResponse
    }

    private fun onGetShopInfo_thenError(error: Throwable) {
        coEvery { gqlGetShopInfoUseCase.executeOnBackground() } coAnswers { throw error }
    }

    private fun onEditProductVariant_thenReturn(response: ProductUpdateV3Response) {
        coEvery { editProductVariantUseCase.execute(any()) } returns response
    }

    private fun onEditProductVariant_thenReturn(error: Throwable) {
        coEvery { editProductVariantUseCase.execute(any()) } throws error
    }

    private fun onGetFreeClaim_thenReturn(deposit: DataDeposit) {
        coEvery {
            topAdsGetShopDepositGraphQLUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DataDeposit>>().onNext(deposit)
        }
    }

    private fun onGetFreeClaim_thenReturn(error: Throwable) {
        coEvery {
            topAdsGetShopDepositGraphQLUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DataDeposit>>().onError(error)
        }
    }

    private fun onGetPopupsInfo_thenReturn(showPopup: Boolean) {
        coEvery {
            popupManagerAddProductUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onNext(showPopup)
        }
    }

    private fun onGetPopupsInfo_thenReturn(error: Throwable) {
        coEvery {
            popupManagerAddProductUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Boolean>>().onError(error)
        }
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

    private fun verifyHideProgressBar() {
        viewModel.viewState.verifyValueEquals(HideProgressDialog)
    }
}