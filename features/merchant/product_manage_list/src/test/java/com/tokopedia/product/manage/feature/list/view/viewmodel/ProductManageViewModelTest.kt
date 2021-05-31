package com.tokopedia.product.manage.feature.list.view.viewmodel

import android.accounts.NetworkErrorException
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.feature.list.data.model.PriceUiModel
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse.*
import com.tokopedia.product.manage.common.feature.list.data.model.TopAdsInfo
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaData
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaResponse
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaWrapper
import com.tokopedia.product.manage.common.feature.list.data.model.filter.Tab
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Data
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Header
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.common.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.common.feature.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult
import com.tokopedia.product.manage.data.createDefaultAccess
import com.tokopedia.product.manage.data.createEditVariantResult
import com.tokopedia.product.manage.data.createGetVariantResponse
import com.tokopedia.product.manage.data.createOptionResponse
import com.tokopedia.product.manage.data.createProduct
import com.tokopedia.product.manage.data.createProductUiModel
import com.tokopedia.product.manage.data.createProductVariant
import com.tokopedia.product.manage.data.createProductVariantResponse
import com.tokopedia.product.manage.data.createSelectionResponse
import com.tokopedia.product.manage.data.createShopOwnerAccess
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.list.data.model.FeaturedProductResponseModel
import com.tokopedia.product.manage.feature.list.data.model.GoldManageFeaturedProductV2
import com.tokopedia.product.manage.feature.list.data.model.Header
import com.tokopedia.product.manage.feature.list.view.model.DeleteProductDialogType
import com.tokopedia.product.manage.feature.list.view.model.FilterTabUiModel.Active
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult.*
import com.tokopedia.product.manage.feature.list.view.model.GetPopUpResult
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByMenu
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByStatus
import com.tokopedia.product.manage.feature.list.view.model.SetFeaturedProductResult
import com.tokopedia.product.manage.feature.list.view.model.ShopInfoResult
import com.tokopedia.product.manage.feature.list.view.model.TopAdsPage
import com.tokopedia.product.manage.feature.list.view.model.ViewState.HideProgressDialog
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.ADD_PRODUCT
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.BROADCAST_CHAT
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.DELETE_PRODUCT
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.DUPLICATE_PRODUCT
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.EDIT_PRICE
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.EDIT_PRODUCT
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.EDIT_STOCK
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.ETALASE_LIST
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.MULTI_SELECT
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.PRODUCT_LIST
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.SET_CASHBACK
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.SET_FEATURED
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.SET_TOP_ADS
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.AccessId.STOCK_REMINDER
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.LocationType.MAIN_LOCATION
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.LocationType.OTHER_LOCATION
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.ProductStatusConstant.STATUS_ACTIVE
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.ProductStatusConstant.STATUS_INACTIVE
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.TopAdsCategory.AUTO_ADS
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.TopAdsCategory.MANUAL_ADS
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModelTest.TopAdsCategory.UNKNOWN_ADS
import com.tokopedia.product.manage.feature.multiedit.data.response.MultiEditProduct
import com.tokopedia.product.manage.feature.multiedit.data.response.MultiEditProductResult
import com.tokopedia.product.manage.feature.multiedit.data.response.MultiEditProductResult.Result
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsResponse
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsResponse.Data
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsResponse.ShopInfoTopAds
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Picture
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Price
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductList
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductListData
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterByCondition.CashBackOnly
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterByCondition.FeaturedOnly
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterByCondition.NewOnly
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOption.SortByName
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOrderOption.ASC
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ProductStockWarehouse
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ShopLocationResponse
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCore
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verifyAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import rx.Subscriber

class ProductManageViewModelTest : ProductManageViewModelTestFixture() {

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
    fun `when edit stock and status success should return edit stock result success`() {
        runBlocking {
            val productId = "0"
            val stock = 0
            val productName = "Amazing Product"
            val status = ProductStatus.ACTIVE
            val productUpdateV3Response = ProductUpdateV3Response(
                productUpdateV3Data = ProductUpdateV3Data(isSuccess = true))
            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )

            onGetWarehouseId_thenReturn(locationList)
            onEditStock_thenReturn(ProductStockWarehouse(status = STATUS_ACTIVE))
            onEditStatus_thenReturn(productUpdateV3Response)

            viewModel.editStock(productId, productName, stock, status)

            val expectedEditStockResult = Success(EditStockResult(productName, productId, stock, status))

            verifyEditStockUseCaseCalled()
            verifyEditStatusUseCaseCalled()
            viewModel.editStockResult.verifySuccessEquals(expectedEditStockResult)
        }
    }

    @Test
    fun `when edit stock success should NOT call edit status and return edit stock result success`() {
        runBlocking {
            val productId = "0"
            val stock = 0
            val productName = "Amazing Product"
            val status = ProductStatus.INACTIVE
            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )

            onGetWarehouseId_thenReturn(locationList)
            onEditStock_thenReturn(ProductStockWarehouse(status = STATUS_INACTIVE))

            viewModel.editStock(productId, productName, stock)

            val expectedEditStockResult = Success(EditStockResult(productName, productId, stock, status))

            verifyEditStockUseCaseCalled()
            verifyEditStatusUseCaseNotCalled()

            viewModel.editStockResult
                .verifySuccessEquals(expectedEditStockResult)
        }
    }

    @Test
    fun `when edit status success should NOT call edit stock and return edit stock result success`() {
        runBlocking {
            val productId = "0"
            val stock = null
            val productName = "Amazing Product"
            val status = ProductStatus.INACTIVE
            val productUpdateV3Response = ProductUpdateV3Response(
                productUpdateV3Data = ProductUpdateV3Data(isSuccess = true))

            onEditStatus_thenReturn(productUpdateV3Response)

            viewModel.editStock(productId, productName, status = status)

            val expectedEditStockResult = Success(EditStockResult(productName, productId, stock, status))

            verifyEditStatusUseCaseCalled()
            verifyEditStockUseCaseNotCalled()

            viewModel.editStockResult
                .verifySuccessEquals(expectedEditStockResult)
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
    fun `when edit stock and status fail should return edit status result fail`() {
        runBlocking {
            val productId = "0"
            val stock = 0
            val productName = "Amazing Product"
            val status = ProductStatus.ACTIVE
            val error = NetworkErrorException()

            onEditStatus_thenReturn(error)
            onEditStock_thenReturn(error)

            viewModel.editStock(productId, productName, stock, status)

            val expectedEditStockResult = Fail(EditStockResult(productName, productId, stock, status, error))

            verifyEditStatusUseCaseCalled()

            viewModel.editStockResult
                .verifyErrorEquals(expectedEditStockResult)
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

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId)

            val topAdsInfo = TopAdsInfo(isTopAds = false, isAutoAds = false)
            val productViewModelList = listOf(createProductUiModel(
                name = "Tolak Angin Madu", minPrice = minPrice, maxPrice = maxPrice, topAds = topAdsInfo, access = createDefaultAccess()))
            val expectedProductList = Success(productViewModelList)

            verifyGetWarehouseIdCalled()

            viewModel.productListResult
                .verifySuccessEquals(expectedProductList)

            viewModel.showStockTicker
                .verifyValueEquals(false)

            verifyHideProgressBar()
        }
    }

    @Test
    fun `given after get product list when getFeaturedProductCount should NOT call getWarehouseId again`() {
        runBlocking {
            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(createProduct(name = "Tolak Angin Madu", price = Price(10000, 100000), pictures = pictures))
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId)
            viewModel.getFeaturedProductCount(shopId)

            verifyGetWarehouseIdCalledOnce()
        }
    }

    @Test
    fun `given main location not found when get product list should set live data success`() {
        runBlocking {
            val shopId = "1500"

            val minPrice = PriceUiModel("10000", "Rp10.000")
            val maxPrice = PriceUiModel("100000", "Rp100.000")
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(createProduct(name = "Tolak Angin Madu", price = Price(10000, 100000), pictures = pictures))
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId)

            val topAdsInfo = TopAdsInfo(isTopAds = false, isAutoAds = false)
            val productViewModelList = listOf(createProductUiModel(
                name = "Tolak Angin Madu", minPrice = minPrice, maxPrice = maxPrice, topAds = topAdsInfo, access = createDefaultAccess()))
            val expectedProductList = Success(productViewModelList)

            verifyGetWarehouseIdCalled()

            viewModel.productListResult
                .verifySuccessEquals(expectedProductList)

            viewModel.showStockTicker
                .verifyValueEquals(false)

            verifyHideProgressBar()
        }
    }

    @Test
    fun `given multi location shop when getProductList more than once should set showStockTicker TRUE`() {
        runBlocking {
            val isMultiLocationShop = true

            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(createProduct(name = "Tolak Angin Madu", price = Price(10000, 100000), pictures = pictures))
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)
            onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)

            viewModel.getProductList(shopId)
            viewModel.getProductList(shopId)

            viewModel.showStockTicker
                .verifyValueEquals(true)
        }
    }

    @Test
    fun `given multi location shop and hideStockTicker called when getProductList again should set showStockTicker FALSE`() {
        runBlocking {
            val isMultiLocationShop = true

            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(createProduct(name = "Tolak Angin Madu", price = Price(10000, 100000), pictures = pictures))
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)
            onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)

            viewModel.getProductList(shopId)
            viewModel.hideStockTicker()
            viewModel.getProductList(shopId)

            viewModel.showStockTicker
                .verifyValueEquals(false)
        }
    }

    @Test
    fun `given multi location shop true when get product list should show stock ticker`() {
        runBlocking {
            val isMultiLocationShop = true
            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(createProduct(name = "Tolak Angin Madu", price = Price(10000, 100000), pictures = pictures))
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)
            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId)

            verifyGetWarehouseIdCalled()
            verifyShowStockTicker()
            verifyHideProgressBar()
        }
    }

    @Test
    fun `given isRefresh true when get product list should set refreshList true`() {
        runBlocking {
            val isRefresh = true

            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(createProduct(name = "Tolak Angin Madu", price = Price(10000, 100000), pictures = pictures))
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId, isRefresh = isRefresh)

            viewModel.refreshList
                .verifyValueEquals(true)
        }
    }

    @Test
    fun `given isShopOwner TRUE when getProductList should map shop owner access into product list`() {
        runBlocking {
            val isShopOwner = true

            val shopId = "1500"
            val minPrice = PriceUiModel("10000", "Rp10.000")
            val maxPrice = PriceUiModel("100000", "Rp100.000")
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(createProduct(name = "Tolak Angin Madu", price = Price(10000, 100000), pictures = pictures))
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )

            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)
            onGetIsShopOwner_thenReturn(isShopOwner)

            viewModel.getProductManageAccess()
            viewModel.getProductList(shopId)

            val topAdsInfo = TopAdsInfo(isTopAds = false, isAutoAds = false)
            val productViewModelList = listOf(createProductUiModel(
                name = "Tolak Angin Madu",
                minPrice = minPrice,
                maxPrice = maxPrice,
                topAds = topAdsInfo,
                access = createShopOwnerAccess()
            ))
            val expectedProductList = Success(productViewModelList)

            viewModel.productListResult
                .verifyValueEquals(expectedProductList)
        }
    }

    @Test
    fun `get product list should fail with exception`() {
        runBlocking {
            val exception = NullPointerException()

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenError(exception)

            viewModel.getProductList("1000")

            val expectedError = Fail(exception)

            verifyGetWarehouseIdCalled()
            viewModel.productListResult
                .verifyErrorEquals(expectedError)
        }
    }

    @Test
    fun `when get filters tab once should map response into ShowFilterTab`() {
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
    fun `when get filters tab more than once should map response into UpdateFilterTab`() {
        val tabs = listOf(Tab(id = "ACTIVE", name = "Active", value = "10"))
        val productListMetaData = ProductListMetaData(tabs = tabs)
        val productListMeta = ProductListMetaWrapper(productListMetaData = productListMetaData)
        val productListMetaResponse = ProductListMetaResponse(productListMeta)

        val filterList = listOf(CashBackOnly, FeaturedOnly)
        val filterOptions = FilterOptionWrapper(filterOptions = filterList, sortOption = SortByName(ASC), selectedFilterCount = 3)

        onGetFiltersTab_thenReturn(productListMetaResponse)

        viewModel.setFilterOptionWrapper(filterOptions)
        viewModel.getFiltersTab()
        viewModel.getFiltersTab()

        val filterTabList = listOf(Active(10))
        val expectedResult = Success(UpdateFilterTab(filterTabList, 10))

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
        val failedResponse = MultiEditProductResult(productID = "2", result = Result(isSuccess = false))
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
        val failedResponse = MultiEditProductResult(productID = "2", result = Result(isSuccess = false))
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

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getFeaturedProductCount(shopId)

            val expectedFeaturedProductCount = Success(1)

            verifyGetWarehouseIdCalled()
            verifyGetProductListCalled()
            viewModel.productListFeaturedOnlyResult.verifySuccessEquals(expectedFeaturedProductCount)
        }
    }

    @Test
    fun `when isPowerMerchant should return power merchant status`() {
        val actualIsPowerMerchant = viewModel.isPowerMerchant()
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
    fun `when edit variant stock success should NOT call edit status and set live data value success`() {
        runBlocking {
            val editStock = true
            val result = createEditVariantResult(editStock = editStock)
            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )

            onGetWarehouseId_thenReturn(locationList)
            onEditStock_thenReturn(ProductStockWarehouse())

            viewModel.editVariantsStock(result)

            val expectedResult = Success(result)

            verifyEditVariantStatusUseCaseNotCalled()
            verifyEditStockUseCaseCalled()

            viewModel.editVariantStockResult
                .verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `when edit variant status success should NOT call edit stock and set live data value success`() {
        val editStatus = true
        val data = ProductUpdateV3Data(isSuccess = true)
        val response = ProductUpdateV3Response(data)
        val result = createEditVariantResult(editStatus = editStatus)

        onEditProductVariant_thenReturn(response)

        viewModel.editVariantsStock(result)

        val expectedResult = Success(result)

        verifyEditVariantStatusUseCaseCalled()
        verifyEditStockUseCaseNotCalled()

        viewModel.editVariantStockResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when edit variant stock and status success should set live data value success`() {
        runBlocking {
            val data = ProductUpdateV3Data(isSuccess = true)
            val response = ProductUpdateV3Response(data)
            val result = createEditVariantResult(editStock = true, editStatus = true)
            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )

            onGetWarehouseId_thenReturn(locationList)
            onEditProductVariant_thenReturn(response)
            onEditStock_thenReturn(ProductStockWarehouse())

            viewModel.editVariantsStock(result)

            val expectedResult = Success(result)

            verifyEditVariantStatusUseCaseCalled()
            verifyEditStockUseCaseCalled()

            viewModel.editVariantStockResult
                .verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `when edit variant status error should set live data value fail`() {
        val errorMessage = "can't edit product :("
        val headerResponse = ProductUpdateV3Header(errorMessage = arrayListOf(errorMessage))
        val data = ProductUpdateV3Data(isSuccess = false, header = headerResponse)
        val response = ProductUpdateV3Response(data)
        val result = createEditVariantResult(editStatus = true)

        onEditProductVariant_thenReturn(response)

        viewModel.editVariantsStock(result)

        val expectedResult = Fail(MessageErrorException(errorMessage))

        viewModel.editVariantStockResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when edit variant stock error should set live data value fail`() {
        runBlocking {
            val error = NullPointerException()
            val result = createEditVariantResult(editStock = true)
            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )

            onGetWarehouseId_thenReturn(locationList)
            onEditStock_thenReturn(error)

            viewModel.editVariantsStock(result)

            val expectedResult = Fail(error)

            viewModel.editVariantStockResult
                .verifyErrorEquals(expectedResult)
        }
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
    fun `when get variants success should set live data value success`() {
        runBlocking {
            val productName = "Tokopedia"
            val variantList = listOf(
                createProductVariantResponse(combination = listOf(0, 1)),
                createProductVariantResponse(combination = listOf(1, 0))
            )
            val firstOption = listOf(
                createOptionResponse(value = "Biru"),
                createOptionResponse(value = "Hijau")
            )
            val secondOption = listOf(
                createOptionResponse(value = "S"),
                createOptionResponse(value = "M")
            )
            val selections = listOf(
                createSelectionResponse(options = firstOption),
                createSelectionResponse(options = secondOption)
            )
            val response = createGetVariantResponse(
                productName,
                products = variantList,
                selections = selections
            )

            val productId = "1400068494"
            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )

            onGetWarehouseId_thenReturn(locationList)
            onGetVariants_thenReturn(response)

            viewModel.getProductVariants(productId)

            val productVariants = listOf(
                createProductVariant(name = "Biru | M", combination = listOf(0, 1), access = createDefaultAccess()),
                createProductVariant(name = "Hijau | S", combination = listOf(1, 0), access = createDefaultAccess())
            )
            val expectedResult = GetVariantResult(productName, productVariants, selections, emptyList())
            val expectedSuccessResult = Success(expectedResult)

            verifyGetVariantsCalled()

            viewModel.getProductVariantsResult.verifySuccessEquals(expectedSuccessResult)
        }
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

    @Test
    fun `when getTopAdsInfo success should set expected top ads info`() {
        testGetTopAdsInfo(
            adsCategory = MANUAL_ADS,
            expectedIsTopAds = true,
            expectedIsAutoAds = false
        )

        testGetTopAdsInfo(
            adsCategory = AUTO_ADS,
            expectedIsTopAds = true,
            expectedIsAutoAds = true
        )

        testGetTopAdsInfo(
            adsCategory = UNKNOWN_ADS,
            expectedIsTopAds = false,
            expectedIsAutoAds = false
        )
    }

    @Test
    fun `when getTopAdsInfo error should set default top ads info false`() {
        onGetShopInfoTopAds_thenReturnError()

        viewModel.getTopAdsInfo()

        val expectedTopAdsInfo = TopAdsInfo(
            isTopAds = false,
            isAutoAds = false
        )

        viewModel.topAdsInfo
            .verifyValueEquals(expectedTopAdsInfo)
    }

    @Test
    fun `given productId when promo top ads clicked should set live data with top ads page`() {
        val productId = "1"

        testOnClickPromoTopAds(
            productId = productId,
            adsCategory = MANUAL_ADS,
            expectedPage = TopAdsPage.ManualAds(productId)
        )

        testOnClickPromoTopAds(
            productId = productId,
            adsCategory = AUTO_ADS,
            expectedPage = TopAdsPage.AutoAds(productId)
        )

        testOnClickPromoTopAds(
            productId = productId,
            adsCategory = UNKNOWN_ADS,
            expectedPage = TopAdsPage.OnBoarding(productId)
        )
    }

    @Test
    fun `given topAdsInfo NULL when promo top ads clicked should set live data with onboarding page`() {
        val productId = "1"

        viewModel.onPromoTopAdsClicked(productId)

        val expectedPage = TopAdsPage.OnBoarding(productId)

        viewModel.onClickPromoTopAds
            .verifyValueEquals(expectedPage)
    }

    @Test
    fun `given user is shop owner when get product manage access should set live data with owner access`() {
        onGetIsShopOwner_thenReturn(isShopOwner = true)

        viewModel.getProductManageAccess()

        val expectedResult = Success(createShopOwnerAccess())

        viewModel.productManageAccess
            .verifySuccessEquals(expectedResult)

        verifyGetProductManageAccessNotCalled()
    }

    @Test
    fun `given user is NOT shop owner when getProductManageAccess should map data from accessIdList`() {
        onGetIsShopOwner_thenReturn(isShopOwner = false)

        testGetProductManageAccess(
            accessList = listOf(Access(ADD_PRODUCT)),
            expectedProductManageAccess = createProductManageAccess(addProduct = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(EDIT_PRODUCT)),
            expectedProductManageAccess = createProductManageAccess(editProduct = true, editPrice = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(ETALASE_LIST)),
            expectedProductManageAccess = createProductManageAccess(etalaseList = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(MULTI_SELECT)),
            expectedProductManageAccess = createProductManageAccess(multiSelect = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(EDIT_STOCK)),
            expectedProductManageAccess = createProductManageAccess(editStock = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(EDIT_PRICE)),
            expectedProductManageAccess = createProductManageAccess(editProduct = true, editPrice = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(DUPLICATE_PRODUCT)),
            expectedProductManageAccess = createProductManageAccess(duplicateProduct = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(STOCK_REMINDER)),
            expectedProductManageAccess = createProductManageAccess(setStockReminder = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(DELETE_PRODUCT)),
            expectedProductManageAccess = createProductManageAccess(deleteProduct = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(SET_TOP_ADS)),
            expectedProductManageAccess = createProductManageAccess(setTopAds = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(SET_CASHBACK)),
            expectedProductManageAccess = createProductManageAccess(setCashBack = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(SET_FEATURED)),
            expectedProductManageAccess = createProductManageAccess(setFeatured = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(PRODUCT_LIST)),
            expectedProductManageAccess = createProductManageAccess(productList = true)
        )

        testGetProductManageAccess(
            accessList = listOf(Access(BROADCAST_CHAT)),
            expectedProductManageAccess = createProductManageAccess(broadCastChat = true)
        )

        testGetProductManageAccess(
            accessList = listOf(),
            expectedProductManageAccess = createProductManageAccess(
                addProduct = false,
                editProduct = false,
                etalaseList = false,
                multiSelect = false,
                editPrice = false,
                editStock = false,
                duplicateProduct = false,
                setStockReminder = false,
                deleteProduct = false,
                setTopAds = false,
                setCashBack = false,
                setFeatured = false,
                productList = false
            )
        )
    }

    @Test
    fun `when get product manage access error should set access live data fail`() {
        val error = IllegalStateException()
        onGetProductManageAccess_thenReturnError(error)

        viewModel.getProductManageAccess()

        val expectedResult = Fail(error)

        viewModel.productManageAccess
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when delete single product should set live data with delete single product data`() {
        val productId = "1"
        val productName = "name"
        val isMultiLocationShop = true

        onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)

        viewModel.onDeleteSingleProduct(productName, productId)

        val expectedDeleteProductDialog = DeleteProductDialogType.SingleProduct(
            productId,
            productName,
            isMultiLocationShop
        )

        viewModel.deleteProductDialog
            .verifyValueEquals(expectedDeleteProductDialog)
    }

    @Test
    fun `when delete multiple product should set live data with delete multiple product data`() {
        val isMultiLocationShop = false

        onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)

        viewModel.onDeleteMultipleProducts()

        val expectedDeleteProductDialog = DeleteProductDialogType.MultipleProduct(isMultiLocationShop)

        viewModel.deleteProductDialog
            .verifyValueEquals(expectedDeleteProductDialog)
    }

    @Test
    fun `given user has add product access when showHideOptionsMenu should set show add product true`() {
        val accessData = Data(listOf(Access(ADD_PRODUCT)))
        val accessResponse = Response(data = accessData)

        onGetProductManageAccess_thenReturn(accessResponse)

        viewModel.getProductManageAccess()
        viewModel.showHideOptionsMenu()

        viewModel.showAddProductOptionsMenu
            .verifyValueEquals(true)

        viewModel.showEtalaseOptionsMenu
            .verifyValueEquals(false)
    }

    @Test
    fun `given user has etalase list access when showHideOptionsMenu should set show etalase menu true`() {
        val accessData = Data(listOf(Access(ETALASE_LIST)))
        val accessResponse = Response(data = accessData)

        onGetProductManageAccess_thenReturn(accessResponse)

        viewModel.getProductManageAccess()
        viewModel.showHideOptionsMenu()

        viewModel.showEtalaseOptionsMenu
            .verifyValueEquals(true)

        viewModel.showAddProductOptionsMenu
            .verifyValueEquals(false)
    }

    @Test
    fun `when hideStockTicker should set showStockTicker false`() {
        viewModel.hideStockTicker()

        viewModel.showStockTicker
            .verifyValueEquals(false)
    }

    private fun testGetProductManageAccess(
        accessList: List<Access>,
        expectedProductManageAccess: ProductManageAccess
    ) {
        val accessData = Data(accessList)
        val accessResponse = Response(data = accessData)

        onGetProductManageAccess_thenReturn(accessResponse)

        viewModel.getProductManageAccess()

        val expectedResult = Success(expectedProductManageAccess)

        viewModel.productManageAccess
            .verifySuccessEquals(expectedResult)
    }

    private fun testOnClickPromoTopAds(
        productId: String,
        adsCategory: Int,
        expectedPage: TopAdsPage
    ) {
        val topAdsData = ShopInfoTopAds(Data(category = adsCategory))
        val response = ShopInfoTopAdsResponse(topAdsData)

        onGetShopInfoTopAds_thenReturn(response)

        viewModel.getTopAdsInfo()
        viewModel.onPromoTopAdsClicked(productId)


        viewModel.onClickPromoTopAds
            .verifyValueEquals(expectedPage)
    }

    private fun testGetTopAdsInfo(
        adsCategory: Int,
        expectedIsTopAds: Boolean,
        expectedIsAutoAds: Boolean
    ) {
        val topAdsData = ShopInfoTopAds(Data(category = adsCategory))
        val response = ShopInfoTopAdsResponse(topAdsData)

        onGetShopInfoTopAds_thenReturn(response)

        viewModel.getTopAdsInfo()

        val expectedTopAdsInfo = TopAdsInfo(expectedIsTopAds, expectedIsAutoAds)

        viewModel.topAdsInfo
            .verifyValueEquals(expectedTopAdsInfo)
    }

    private fun onGetVariants_thenReturn(response: GetProductVariantResponse) {
        coEvery { getProductVariantUseCase.execute(any()) } returns response
    }

    private fun verifyGetVariantsCalled() {
        coVerify { getProductVariantUseCase.execute(any()) }
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

    private suspend fun onEditStock_thenReturn(response: ProductStockWarehouse) {
        coEvery { editStockUseCase.execute(any()) } returns response
    }

    private suspend fun onEditStock_thenReturn(error: Throwable) {
        coEvery { editStockUseCase.execute(any()) } throws error
    }

    private suspend fun onEditStatus_thenReturn(productUpdateV3Response: ProductUpdateV3Response) {
        coEvery { editStatusUseCase.executeOnBackground() } returns productUpdateV3Response
    }

    private suspend fun onEditStatus_thenReturn(error: Throwable) {
        coEvery { editStatusUseCase.executeOnBackground() } throws error
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
        coEvery { getShopInfoTopAdsUseCase.execute(any()) } returns shopInfoTopAdsResponse
    }

    private fun onGetShopInfoTopAds_thenReturnError() {
        coEvery { getShopInfoTopAdsUseCase.execute(any()) } throws Throwable()
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

    private fun onGetIsShopOwner_thenReturn(isShopOwner: Boolean) {
        coEvery {
            userSessionInterface.isShopOwner
        } returns isShopOwner
    }

    private fun onGetProductManageAccess_thenReturn(response: Response) {
        coEvery {
            getProductManageAccessUseCase.execute(any())
        } returns response
    }

    private fun onGetProductManageAccess_thenReturnError(error: Throwable) {
        coEvery {
            getProductManageAccessUseCase.execute(any())
        } throws error
    }

    private fun onGetIsMultiLocationShop_thenReturn(isMultiLocationShop: Boolean) {
        coEvery {
            userSessionInterface.isMultiLocationShop
        } returns isMultiLocationShop
    }

    private fun onGetWarehouseId_thenReturn(locationList: List<ShopLocationResponse>) {
        coEvery {
            getAdminInfoShopLocationUseCase.execute(any())
        } returns locationList
    }

    private fun verifyEditPriceUseCaseCalled() {
        coVerify { editPriceUseCase.executeOnBackground() }
    }

    private fun verifyEditStatusUseCaseCalled() {
        coVerify { editStatusUseCase.executeOnBackground() }
    }

    private fun verifyEditStatusUseCaseNotCalled() {
        coVerify(exactly = 0) { editStatusUseCase.executeOnBackground() }
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

    private fun verifyGetProductManageAccessNotCalled() {
        coVerify(exactly = 0) { getProductManageAccessUseCase.execute(any()) }
    }

    private fun verifyGetWarehouseIdCalled() {
        coVerify { getAdminInfoShopLocationUseCase.execute(any()) }
    }

    private fun verifyGetWarehouseIdCalledOnce() {
        coVerify(exactly = 1) { getAdminInfoShopLocationUseCase.execute(any()) }
    }

    private fun verifyEditStockUseCaseCalled() {
        coVerify { editStockUseCase.execute(any()) }
    }

    private fun verifyEditStockUseCaseNotCalled() {
        coVerify(exactly = 0) { editStockUseCase.execute(any()) }
    }

    private fun verifyEditVariantStatusUseCaseCalled() {
        coVerify { editProductVariantUseCase.execute(any()) }
    }

    private fun verifyEditVariantStatusUseCaseNotCalled() {
        coVerify(exactly = 0) { editProductVariantUseCase.execute(any()) }
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

    private fun verifyShowStockTicker() {
        viewModel.showStockTicker.verifyValueEquals(true)
    }

    private fun createProductManageAccess(
        addProduct: Boolean = false,
        editProduct: Boolean = false,
        etalaseList: Boolean = false,
        multiSelect: Boolean = false,
        editPrice: Boolean = false,
        editStock: Boolean = false,
        duplicateProduct: Boolean = false,
        setStockReminder: Boolean = false,
        deleteProduct: Boolean = false,
        setTopAds: Boolean = false,
        setCashBack: Boolean = false,
        setFeatured: Boolean = false,
        productList: Boolean = false,
        broadCastChat: Boolean = false
    ): ProductManageAccess {
        return ProductManageAccess(
            addProduct,
            editProduct,
            etalaseList,
            multiSelect,
            editPrice,
            editStock,
            duplicateProduct,
            setStockReminder,
            deleteProduct,
            setTopAds,
            setCashBack,
            setFeatured,
            productList,
            broadCastChat
        )
    }

    private object AccessId {
        const val ADD_PRODUCT = "101"
        const val EDIT_PRODUCT = "121"
        const val ETALASE_LIST = "106"
        const val MULTI_SELECT = "107"
        const val EDIT_STOCK = "124"
        const val EDIT_PRICE = "121"
        const val DUPLICATE_PRODUCT = "123"
        const val STOCK_REMINDER = "109"
        const val DELETE_PRODUCT = "16"
        const val SET_TOP_ADS = "114"
        const val SET_CASHBACK = "116"
        const val SET_FEATURED = "113"
        const val PRODUCT_LIST = "100"
        const val BROADCAST_CHAT = "115"
    }

    private object TopAdsCategory {
        const val MANUAL_ADS = 3
        const val AUTO_ADS = 4
        const val UNKNOWN_ADS = -1
    }

    private object LocationType {
        const val MAIN_LOCATION = 1
        const val OTHER_LOCATION = 99
    }

    private object ProductStatusConstant {
        const val STATUS_ACTIVE = 1
        const val STATUS_INACTIVE = 3
    }
}