package com.tokopedia.product.manage.feature.list.view.viewmodel

import android.accounts.NetworkErrorException
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.feature.getstatusshop.data.model.StatusInfo
import com.tokopedia.product.manage.common.feature.getstatusshop.domain.GetStatusShopUseCase
import com.tokopedia.product.manage.common.feature.list.data.model.PriceUiModel
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse.*
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.list.data.model.TopAdsInfo
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaData
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaResponse
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaWrapper
import com.tokopedia.product.manage.common.feature.list.data.model.filter.Tab
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Data
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Header
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.common.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.common.feature.uploadstatus.constant.UploadStatusType
import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.entity.UploadStatusEntity
import com.tokopedia.product.manage.common.feature.uploadstatus.domain.ClearUploadStatusUseCase
import com.tokopedia.product.manage.common.feature.uploadstatus.domain.GetUploadStatusUseCase
import com.tokopedia.product.manage.common.feature.uploadstatus.util.UploadStatusMapper.convertToModel
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
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.list.data.model.FeaturedProductResponseModel
import com.tokopedia.product.manage.feature.list.data.model.GetTargetedTickerResponse
import com.tokopedia.product.manage.feature.list.data.model.GoldManageFeaturedProductV2
import com.tokopedia.product.manage.feature.list.data.model.Header
import com.tokopedia.product.manage.feature.list.data.model.ProductArchivalInfo
import com.tokopedia.product.manage.feature.list.data.model.ShopWarehouseResponse
import com.tokopedia.product.manage.feature.list.data.repository.MockedUploadStatusRepository
import com.tokopedia.product.manage.feature.list.data.repository.MockedUploadStatusRepositoryException
import com.tokopedia.product.manage.feature.list.view.model.DeleteProductDialogType
import com.tokopedia.product.manage.feature.list.view.model.FilterTabUiModel.Active
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult.ShowFilterTab
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult.UpdateFilterTab
import com.tokopedia.product.manage.feature.list.view.model.GetPopUpResult
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByMenu
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByStatus
import com.tokopedia.product.manage.feature.list.view.model.SetFeaturedProductResult
import com.tokopedia.product.manage.feature.list.view.model.ShopInfoResult
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
import com.tokopedia.product.manage.feature.suspend.view.uimodel.SuspendReasonUiModel
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_STOCK_AVAILABLE
import com.tokopedia.shop.common.data.source.cloud.model.MaxStockThresholdResponse
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsResponse
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsResponse.Data
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoTopAdsResponse.ShopInfoTopAds
import com.tokopedia.shop.common.data.source.cloud.model.productlist.*
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
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.util.concurrent.TimeUnit

class ProductManageViewModelTest : ProductManageViewModelTestFixture() {

    @Test
    fun `when editPrice success should return_edit price success result`() {
        runBlocking {
            val productId = "0"
            val price = "10000"
            val productName = "Amazing Product"
            val productUpdateV3Response = ProductUpdateV3Response(
                productUpdateV3Data =
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
                productUpdateV3Data = ProductUpdateV3Data(isSuccess = true)
            )
            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )

            onGetWarehouseId_thenReturn(locationList)
            onEditStock_thenReturn(ProductStockWarehouse(status = STATUS_ACTIVE))
            onEditStatus_thenReturn(productUpdateV3Response)

            viewModel.editStock(productId, productName, stock, status)

            val expectedEditStockResult =
                Success(EditStockResult(productName, productId, stock, status))

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

            val expectedEditStockResult =
                Success(EditStockResult(productName, productId, stock, status))

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
                productUpdateV3Data = ProductUpdateV3Data(isSuccess = true)
            )

            onEditStatus_thenReturn(productUpdateV3Response)

            viewModel.editStock(productId, productName, status = status)

            val expectedEditStockResult =
                Success(EditStockResult(productName, productId, stock, status))

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
            val productUpdateV3Response = ProductUpdateV3Response(
                productUpdateV3Data =
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
            val productUpdateV3Response = ProductUpdateV3Response(
                productUpdateV3Data =
                ProductUpdateV3Data(isSuccess = false)
            )

            onEditPrice_thenReturn(productUpdateV3Response)

            viewModel.editPrice(productId, price, productName)

            val error = NetworkErrorException()
            val expectedEditPriceResult =
                Fail(EditPriceResult(productName, productId, price, error))

            verifyEditPriceUseCaseCalled()
            viewModel.editPriceResult.verifyErrorEquals(expectedEditPriceResult)
        }
    }

    @Test
    fun `when editPrice fails with error message should set live data value fail`() {
        runBlocking {
            val productId = "0"
            val price = "10000"
            val productName = "Amazing Product"
            val errorMessage = "Error"
            val productUpdateV3Response = ProductUpdateV3Response(
                productUpdateV3Data = ProductUpdateV3Data(
                    isSuccess = false,
                    header = ProductUpdateV3Header(
                        errorMessage = arrayListOf(errorMessage)
                    )
                )
            )
            onEditPrice_thenReturn(productUpdateV3Response)

            viewModel.editPrice(productId, price, productName)

            verifyEditPriceUseCaseCalled()
            val expectedThrowable = Throwable(message = errorMessage)
            val expectedEditPriceResult =
                Fail(EditPriceResult(productName, productId, price, expectedThrowable))
            viewModel.editPriceResult.verifyErrorEquals(expectedEditPriceResult)
        }
    }

    @Test
    fun `when editPrice error should set live data value fail`() {
        runBlocking {
            val productId = "0"
            val price = "10000"
            val productName = "Amazing Product"
            val error = NullPointerException()
            onEditPrice_thenError(error)

            viewModel.editPrice(productId, price, productName)

            verifyEditPriceUseCaseCalled()
            val expectedError = NetworkErrorException()
            val expectedEditPriceResult =
                Fail(EditPriceResult(productName, productId, price, expectedError))
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

            val expectedEditStockResult =
                Fail(EditStockResult(productName, productId, stock, status, error))

            verifyEditStatusUseCaseCalled()

            viewModel.editStockResult
                .verifyErrorEquals(expectedEditStockResult)
        }
    }

    @Test
    fun `when edit status fail with error message should set live data value fail`() {
        runBlocking {
            val productId = "0"
            val stock = 0
            val productName = "Amazing Product"
            val status = ProductStatus.ACTIVE
            val errorMessage = "Error"
            val statusResponse = ProductUpdateV3Response(
                productUpdateV3Data = ProductUpdateV3Data(
                    isSuccess = false,
                    header = ProductUpdateV3Header(
                        errorMessage = arrayListOf(errorMessage)
                    )
                )
            )

            onEditStock_thenReturn(ProductStockWarehouse())
            onEditStatus_thenReturn(statusResponse)

            viewModel.editStock(productId, productName, stock, status)

            val expectedThrowable = Throwable(message = errorMessage)
            val expectedEditStockResult =
                Fail(EditStockResult(productName, productId, stock, status, expectedThrowable))
            verifyEditStatusUseCaseCalled()
            viewModel.editStockResult
                .verifyErrorEquals(expectedEditStockResult)
        }
    }

    @Test
    fun `when edit status fail without error message should set live data value fail`() {
        runBlocking {
            val productId = "0"
            val stock = 0
            val productName = "Amazing Product"
            val status = ProductStatus.ACTIVE
            val statusResponse = ProductUpdateV3Response(
                productUpdateV3Data = ProductUpdateV3Data(
                    isSuccess = false
                )
            )

            onEditStock_thenReturn(ProductStockWarehouse())
            onEditStatus_thenReturn(statusResponse)

            viewModel.editStock(productId, productName, stock, status)

            val expectedThrowable = NetworkErrorException()
            val expectedEditStockResult =
                Fail(EditStockResult(productName, productId, stock, status, expectedThrowable))
            verifyEditStatusUseCaseCalled()
            viewModel.editStockResult
                .verifyErrorEquals(expectedEditStockResult)
        }
    }

    @Test
    fun `when edit stock status response is inactive but current status is Active, should set status from response instead`() {
        runBlocking {
            val productId = "0"
            val stock = 0
            val productName = "Amazing Product"
            val status = ProductStatus.ACTIVE
            val statusResponse = ProductUpdateV3Response(
                productUpdateV3Data = ProductUpdateV3Data(
                    isSuccess = false
                )
            )
            val stockResponse = ProductStockWarehouse(status = STATUS_INACTIVE)
            onEditStock_thenReturn(stockResponse)
            onEditStatus_thenReturn(statusResponse)

            viewModel.editStock(productId, productName, stock, status)

            assert((viewModel.editStockResult.value as? Success)?.data?.status != status)
        }
    }

    @Test
    fun `when edit stock status response is not valid, should set status from current one instead`() {
        runBlocking {
            val productId = "0"
            val stock = 0
            val productName = "Amazing Product"
            val status = ProductStatus.ACTIVE
            val statusResponse = ProductUpdateV3Response(
                productUpdateV3Data = ProductUpdateV3Data(
                    isSuccess = true
                )
            )
            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val stockResponse = ProductStockWarehouse(status = 123)
            onEditStock_thenReturn(stockResponse)
            onEditStatus_thenReturn(statusResponse)
            onGetWarehouseId_thenReturn(locationList)

            viewModel.editStock(productId, productName, stock, status)

            assert((viewModel.editStockResult.value as? Success)?.data?.status == status)
        }
    }

    @Test
    fun `when deleteProduct fail with error message should return delete product fail result`() {
        runBlocking {
            val productId = "0"
            val productName = "Amazing Product"
            val errorMessage = "Error"
            val productUpdateV3Response = ProductUpdateV3Response(
                productUpdateV3Data = ProductUpdateV3Data(
                    isSuccess = false,
                    header = ProductUpdateV3Header(
                        errorMessage = arrayListOf(errorMessage)
                    )
                )
            )

            onDeleteProduct_thenReturn(productUpdateV3Response)

            viewModel.deleteSingleProduct(productName, productId)

            val error = NetworkErrorException()
            val expectedDeleteProductResult =
                Fail(DeleteProductResult(productName, productId, error))

            verifyDeleteProductUseCaseCalled()
            viewModel.deleteProductResult.verifyErrorEquals(expectedDeleteProductResult)
        }
    }

    @Test
    fun `when deleteProduct fail should return delete product fail result`() {
        runBlocking {
            val productId = "0"
            val productName = "Amazing Product"
            val productUpdateV3Response = ProductUpdateV3Response(
                productUpdateV3Data =
                ProductUpdateV3Data(isSuccess = false)
            )

            onDeleteProduct_thenReturn(productUpdateV3Response)

            viewModel.deleteSingleProduct(productName, productId)

            val error = NetworkErrorException()
            val expectedDeleteProductResult =
                Fail(DeleteProductResult(productName, productId, error))

            verifyDeleteProductUseCaseCalled()
            viewModel.deleteProductResult.verifyErrorEquals(expectedDeleteProductResult)
        }
    }

    @Test
    fun `when deleteProduct error should set live data value fail`() {
        runBlocking {
            val productId = "0"
            val productName = "Amazing Product"
            val error = NullPointerException()
            onDeleteProduct_thenError(error)

            viewModel.deleteSingleProduct(productName, productId)

            val expectedDeleteProductResult =
                Fail(DeleteProductResult(productName, productId, error))
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
            listOf(true, true, false, false),
            ProductManageFilterMapper.countSelectedFilter(selectedFilter)
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
            listOf(true, true, false, true)
        )
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
            listOf(true, true, false, true)
        )

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
            val featuredProductResponseModel =
                FeaturedProductResponseModel(goldManageFeaturedProductV2)

            onSetFeaturedProduct_thenReturn(featuredProductResponseModel)

            viewModel.setFeaturedProduct(productId, status)

            val expectedFeaturedProductResult = Success(SetFeaturedProductResult(productId, status))

            verifySetFeaturedProductUseCaseCalled()
            verifySetFeaturedProductResponseEquals(expectedFeaturedProductResult)
        }
    }

    @Test
    fun `when setFeaturedProduct error should set live data value fail`() {
        runBlocking {
            val productId = "123"
            val status = 1
            val error = NullPointerException()
            onSetFeaturedProduct_thenError(error)

            viewModel.setFeaturedProduct(productId, status)

            verifySetFeaturedProductUseCaseCalled()
            viewModel.setFeaturedProductResult.verifyErrorEquals(Fail(error))
        }
    }

    @Test
    fun `get product list should map product to view model`() {
        runBlocking {
            val shopId = "1500"

            val minPrice = PriceUiModel("10000", "Rp10.000")
            val maxPrice = PriceUiModel("100000", "Rp100.000")
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val paramsProductList = createFilterOptions(1)
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId, filterOptions = paramsProductList)

            val topAdsInfo = TopAdsInfo(isTopAds = false, isAutoAds = false)
            val productViewModelList = listOf(
                createProductUiModel(
                    name = "Tolak Angin Madu",
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    topAds = topAdsInfo,
                    access = createDefaultAccess()
                )
            )
            val expectedProductList = Success(productViewModelList)

            verifyGetWarehouseIdCalled()

            viewModel.productListResult
                .verifySuccessEquals(expectedProductList)

            viewModel.showTicker
                .verifyValueEquals(null)

            verifyHideProgressBar()
        }
    }


    @Test
    fun `get product list archival should map product to view model`() {
        runBlocking {
            val shopId = "1500"

            val minPrice = PriceUiModel("10000", "Rp10.000")
            val maxPrice = PriceUiModel("100000", "Rp100.000")
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val paramsProductList = createFilterOptions(1)
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)
            paramsProductList.add(FilterOption.FilterByCondition.ProductPotentialArchivedStatus)

            viewModel.getProductList(shopId, filterOptions = paramsProductList)

            val topAdsInfo = TopAdsInfo(isTopAds = false, isAutoAds = false)
            val productViewModelList = listOf(
                createProductUiModel(
                    name = "Tolak Angin Madu",
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    topAds = topAdsInfo,
                    access = createDefaultAccess()
                )
            )
            val expectedProductList = Success(productViewModelList)

            verifyGetWarehouseIdCalled()

            viewModel.productListResult
                .verifySuccessEquals(expectedProductList)

            viewModel.showTicker
                .verifyValueEquals(null)

            verifyHideProgressBar()
        }
    }

    @Test
    fun `get product list arhival or potential archival should map product to view model`() {
        runBlocking {
            val shopId = "1500"

            val minPrice = PriceUiModel("10000", "Rp10.000")
            val maxPrice = PriceUiModel("100000", "Rp100.000")
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val paramsProductList = createFilterOptions(1)
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)
            paramsProductList.add(FilterOption.FilterByCondition.ProductPotentialArchivedStatus)
            paramsProductList.add(FilterOption.FilterByCondition.ProductArchival)

            viewModel.getProductList(shopId, filterOptions = paramsProductList)

            val topAdsInfo = TopAdsInfo(isTopAds = false, isAutoAds = false)
            val productViewModelList = listOf(
                createProductUiModel(
                    name = "Tolak Angin Madu",
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    topAds = topAdsInfo,
                    access = createDefaultAccess()
                )
            )
            val expectedProductList = Success(productViewModelList)

            verifyGetWarehouseIdCalled()

            viewModel.productListResult
                .verifySuccessEquals(expectedProductList)

            viewModel.showTicker
                .verifyValueEquals(null)

            verifyHideProgressBar()
        }
    }

    @Test
    fun `given get max stock success, get product list should map product to view model`() {
        runBlocking {
            val shopId = "1500"

            val minPrice = PriceUiModel("10000", "Rp10.000")
            val maxPrice = PriceUiModel("100000", "Rp100.000")
            val pictures = listOf(Picture("imageUrl"))

            val maxStock = 5

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val paramsProductList = createFilterOptions(1)
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)
            onGetMaxStockThreshold_thenReturn(maxStock)

            viewModel.getProductList(shopId, filterOptions = paramsProductList)

            val topAdsInfo = TopAdsInfo(isTopAds = false, isAutoAds = false)
            val productViewModelList = listOf(
                createProductUiModel(
                    name = "Tolak Angin Madu",
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    topAds = topAdsInfo,
                    access = createDefaultAccess(),
                    maxStock = maxStock
                )
            )
            val expectedProductList = Success(productViewModelList)

            verifyGetWarehouseIdCalled()

            viewModel.productListResult
                .verifySuccessEquals(expectedProductList)

            viewModel.showTicker
                .verifyValueEquals(null)

            verifyHideProgressBar()
        }
    }

    @Test
    fun `get product list null should set live data to empty`() {
        runBlocking {
            val shopId = "1500"

            val productListData = ProductListData(null)

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val paramsProductList = createFilterOptions(1)
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId, filterOptions = paramsProductList)

            verifyGetWarehouseIdCalled()

            viewModel.productListResult
                .verifySuccessEquals(Success(listOf<ProductUiModel>()))

            viewModel.showTicker
                .verifyValueEquals(null)

            verifyHideProgressBar()
        }
    }

    @Test
    fun `get product list with delay should map product to view model`() {
        runBlocking {
            val shopId = "1500"

            val minPrice = PriceUiModel("10000", "Rp10.000")
            val maxPrice = PriceUiModel("100000", "Rp100.000")
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val paramsProductList = createFilterOptions(1)
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId, filterOptions = paramsProductList, withDelay = true)

            val topAdsInfo = TopAdsInfo(isTopAds = false, isAutoAds = false)
            val productViewModelList = listOf(
                createProductUiModel(
                    name = "Tolak Angin Madu",
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    topAds = topAdsInfo,
                    access = createDefaultAccess()
                )
            )
            val expectedProductList = Success(productViewModelList)

            viewModel.productListResult.getOrAwaitValue(1500L, TimeUnit.MILLISECONDS).let {
                assert(it == expectedProductList)
            }
        }
    }

    @Test
    fun `given after get product list when getFeaturedProductCount should NOT call getWarehouseId again`() {
        runBlocking {
            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
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

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))
            val paramsProductList = createFilterOptions(1)

            val locationList = listOf(
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId, filterOptions = paramsProductList)

            val topAdsInfo = TopAdsInfo(isTopAds = false, isAutoAds = false)
            val productViewModelList = listOf(
                createProductUiModel(
                    name = "Tolak Angin Madu",
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    topAds = topAdsInfo,
                    access = createDefaultAccess()
                )
            )
            val expectedProductList = Success(productViewModelList)

            verifyGetWarehouseIdCalled()

            viewModel.productListResult
                .verifySuccessEquals(expectedProductList)

            viewModel.showTicker
                .verifyValueEquals(null)

            verifyHideProgressBar()
        }
    }

    @Test
    fun `given non-empty ticker data when getProductList more than once should set showStockTicker TRUE`() {
        runBlocking {
            val isMultiLocationShop = true

            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val paramsProductList = createFilterOptions(1)

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )

            val tickerResponse = GetTargetedTickerResponse(
                GetTargetedTickerResponse.GetTargetedTicker(
                    listOf(mockk())
                )
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)
            onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)
            onGetTickerList_thenReturn(tickerResponse)
            onGetTickerData_thenReturn(listOf(mockk()), tickerResponse)
            onGetIsShowStockAvailableTicker(true)
            val statusShop = StatusInfo("1", "", "", "")
            onGetStatusShop_thenReturn(statusShop)

            val accessData = Data(listOf(Access(ADD_PRODUCT)))
            val accessResponse = Response(data = accessData)

            onGetProductManageAccess_thenReturn(accessResponse)
            viewModel.getProductManageAccess()
            viewModel.getTickerData()
            viewModel.getProductList(shopId, paramsProductList)

            viewModel.showTicker
                .verifyValueEquals(true)
        }
    }

    @Test
    fun `given non-empty ticker data and hideStockTicker called when getProductList again should set showStockTicker TRUE`() {
        runBlocking {
            val isMultiLocationShop = true

            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val tickerResponse = GetTargetedTickerResponse(
                GetTargetedTickerResponse.GetTargetedTicker(
                    listOf(mockk())
                )
            )

            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)
            onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)
            onGetTickerList_thenReturn(tickerResponse)
            onGetTickerData_thenReturn(listOf(mockk()), tickerResponse)
            onGetIsShowStockAvailableTicker(true)

            val statusShop = StatusInfo("1", "", "", "")
            onGetStatusShop_thenReturn(statusShop)

            val accessData = Data(listOf(Access(ADD_PRODUCT)))
            val accessResponse = Response(data = accessData)

            onGetProductManageAccess_thenReturn(accessResponse)
            viewModel.getProductManageAccess()
            viewModel.getTickerData()
            viewModel.getProductList(shopId)
            viewModel.hideTicker()
            viewModel.getProductList(shopId)

            viewModel.showTicker
                .verifyValueEquals(true)
        }
    }

    @Test
    fun `given empty ticker data when getProductList more than once should set showStockTicker TRUE`() {
        runBlocking {
            val isMultiLocationShop = true

            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val paramsProductList = createFilterOptions(1)

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val tickerResponse = GetTargetedTickerResponse(
                null
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)
            onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)
            onGetTickerList_thenReturn(tickerResponse)
            onGetTickerData_thenReturn(emptyList(), tickerResponse)

            viewModel.getTickerData()
            viewModel.getProductList(shopId, paramsProductList)

            viewModel.showTicker
                .verifyValueEquals(null)
        }
    }

    @Test
    fun `given empty ticker data and hideStockTicker called when getProductList again should set showStockTicker TRUE`() {
        runBlocking {
            val isMultiLocationShop = true

            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val tickerResponse = GetTargetedTickerResponse(
                GetTargetedTickerResponse.GetTargetedTicker(
                    listOf(mockk())
                )
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)
            onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)
            onGetTickerList_thenReturn(tickerResponse)
            onGetTickerData_thenReturn(emptyList(), tickerResponse)
            viewModel.getTickerData()
            viewModel.getProductList(shopId)
            viewModel.hideTicker()
            viewModel.getProductList(shopId)

            viewModel.showTicker
                .verifyValueEquals(false)
        }
    }

    @Test
    fun `given non-empty ticker data and non success product list when showStockTicker should not set showStockTicker TRUE`() {
        runBlocking {
            onGetTickerData_thenReturn(listOf(mockk()), mockk())

            viewModel.getTickerData()
            viewModel.showTicker()

            viewModel.showTicker
                .verifyValueEquals(null)
        }
    }

    @Test
    fun `given non-empty ticker data when get product list should show stock ticker`() {
        runBlocking {
            val isMultiLocationShop = true
            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))
            val paramsProductList = createFilterOptions(1)

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )

            onGetWarehouseId_thenReturn(locationList)
            onGetIsShowStockAvailableTicker(true)
            onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)

            onGetProductList_thenReturn(productListData)
            onGetTickerData_thenReturn(listOf(mockk()), mockk())
            val statusShop = StatusInfo("1", "", "", "")
            onGetStatusShop_thenReturn(statusShop)

            val accessData = Data(listOf(Access(ADD_PRODUCT)))
            val accessResponse = Response(data = accessData)

            onGetProductManageAccess_thenReturn(accessResponse)
            viewModel.getProductManageAccess()
            viewModel.getTickerData()
            viewModel.getProductList(shopId, filterOptions = paramsProductList)

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

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
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

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
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
            val productViewModelList = listOf(
                createProductUiModel(
                    name = "Tolak Angin Madu",
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    topAds = topAdsInfo,
                    access = createShopOwnerAccess()
                )
            )
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
    fun `when get product list failed with CancellationException, should not set live data value`() {
        runBlocking {
            val exception = CancellationException()

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenError(exception)

            viewModel.getProductList("1000")

            verifyGetWarehouseIdCalled()
            assert(viewModel.productListResult.value !is Fail)
        }
    }

    @Test
    fun `when get filters tab once should map response into ShowFilterTab`() {
        val tabs = listOf(Tab(id = "ACTIVE", name = "Active", value = "10"))
        val productListMetaData = ProductListMetaData(tabs = tabs)
        val productListMeta = ProductListMetaWrapper(productListMetaData = productListMetaData)
        val productListMetaResponse = ProductListMetaResponse(productListMeta)

        onGetFiltersTab_thenReturn(productListMetaResponse)
        viewModel.getFiltersTab()

        val filterTabList = listOf(Active(10))
        val expectedResult = Success(ShowFilterTab(filterTabList))

        viewModel.productFiltersTab
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get filters tab with delay should map response into ShowFilterTab`() {
        runBlocking {
            val tabs = listOf(Tab(id = "ACTIVE", name = "Active", value = "10"))
            val productListMetaData = ProductListMetaData(tabs = tabs)
            val productListMeta = ProductListMetaWrapper(productListMetaData = productListMetaData)
            val productListMetaResponse = ProductListMetaResponse(productListMeta)

            onGetFiltersTab_thenReturn(productListMetaResponse)

            val filterList = listOf(CashBackOnly, FeaturedOnly)
            val filterOptions = FilterOptionWrapper(
                filterOptions = filterList,
                sortOption = SortByName(ASC),
                selectedFilterCount = 3
            )
            viewModel.setFilterOptionWrapper(filterOptions)
            viewModel.getFiltersTab(true)

            val filterTabList = listOf(Active(10))
            val expectedResult = Success(ShowFilterTab(filterTabList))

            viewModel.productFiltersTab.getOrAwaitValue(1500L, TimeUnit.MILLISECONDS).let {
                assert(it == expectedResult)
            }
        }
    }

    @Test
    fun `when get filters tab more than once should map response into UpdateFilterTab`() {
        val tabs = listOf(Tab(id = "ACTIVE", name = "Active", value = "10"))
        val productListMetaData = ProductListMetaData(tabs = tabs)
        val productListMeta = ProductListMetaWrapper(productListMetaData = productListMetaData)
        val productListMetaResponse = ProductListMetaResponse(productListMeta)

        val filterList = listOf(CashBackOnly, FeaturedOnly)
        val filterOptions = FilterOptionWrapper(
            filterOptions = filterList,
            sortOption = SortByName(ASC),
            selectedFilterCount = 3
        )

        onGetFiltersTab_thenReturn(productListMetaResponse)

        viewModel.setFilterOptionWrapper(filterOptions)
        viewModel.getFiltersTab()
        viewModel.getFiltersTab()

        val filterTabList = listOf(Active(10))
        val expectedResult = Success(UpdateFilterTab(filterTabList))

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
    fun `when get filters tab fail with CancellationException, should not set live data value`() {
        runBlocking {
            val exception = CancellationException()

            onGetFiltersTab_thenError(exception)

            viewModel.getFiltersTab()

            assert(viewModel.productFiltersTab.value !is Fail)
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
    fun `get gold merchant status false should set shop info result success and false`() {
        runBlocking {
            val isGoldMerchant = false
            val isOfficialStore = false
            val shopDomain = "http://www.tokopedia.com/#1"
            val shopCore = ShopCore(domain = shopDomain)
            val goldOS = ShopInfo.GoldOS(isGold = 0, isOfficial = 0)

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
        val successResponse =
            MultiEditProductResult(productID = "1", result = Result(isSuccess = true))
        val failedResponse =
            MultiEditProductResult(productID = "2", result = Result(isSuccess = false))

        val response = MultiEditProduct(listOf(successResponse, failedResponse))
        val shopWarehouseResponse = ShopWarehouseResponse(
            ShopWarehouseResponse.KeroWarehouseShop(
                data = ShopWarehouseResponse.KeroWarehouseShop.Data(
                    listOf()
                )
            )
        )
        onShopWarehouse_thenReturn(shopWarehouseResponse)
        onMultiEditProducts_thenReturn(response)

        viewModel.editProductsByStatus(productIds = listOf("1", "2"), status = status)

        val expectedResult =
            Success(
                EditByStatus(
                    status,
                    listOf(successResponse),
                    listOf(failedResponse),
                    listOf()
                )
            )

        viewModel.multiEditProductResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `edit multiple products by status should return success but empty result response`() {
        val status = ProductStatus.ACTIVE
        val response = MultiEditProduct()

        val shopWarehouseResponse = ShopWarehouseResponse(
            ShopWarehouseResponse.KeroWarehouseShop(
                data = ShopWarehouseResponse.KeroWarehouseShop.Data(
                    listOf()
                )
            )
        )
        onShopWarehouse_thenReturn(shopWarehouseResponse)
        onMultiEditProducts_thenReturn(response)

        viewModel.editProductsByStatus(productIds = listOf(anyString(), anyString()), status = status)

        val expectedResult = Success(EditByStatus(status, listOf(), listOf(), listOf()))

        viewModel.multiEditProductResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `edit multiple products by status should fail with exception`() {
        val status = ProductStatus.ACTIVE
        val exception = NullPointerException()

        val shopWarehouseResponse = ShopWarehouseResponse(
            ShopWarehouseResponse.KeroWarehouseShop(
                data = ShopWarehouseResponse.KeroWarehouseShop.Data(
                    listOf()
                )
            )
        )
        onShopWarehouse_thenReturn(shopWarehouseResponse)
        onMultiEditProducts_thenError(exception)

        viewModel.editProductsByStatus(productIds = listOf("1", "2"), status = status)

        val expectedError = Fail(exception)

        viewModel.multiEditProductResult
            .verifyErrorEquals(expectedError)
    }

    @Test
    fun `edit multiple products by etalase should return success and failed response`() {
        val menuId = "1"
        val menuName = "Etalase Toko"
        val successResponse =
            MultiEditProductResult(productID = "1", result = Result(isSuccess = true))
        val failedResponse =
            MultiEditProductResult(productID = "2", result = Result(isSuccess = false))

        val response = MultiEditProduct(listOf(successResponse, failedResponse))

        onMultiEditProducts_thenReturn(response)

        viewModel.editProductsEtalase(listOf("1", "2"), menuId, menuName)

        val expectedResult =
            Success(EditByMenu(menuId, menuName, listOf(successResponse), listOf(failedResponse)))

        viewModel.multiEditProductResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `edit multiple products by etalase should return success but empty result response`() {
        val menuId = "1"
        val menuName = "Etalase Toko"
        val response = MultiEditProduct(null)

        onMultiEditProducts_thenReturn(response)

        viewModel.editProductsEtalase(listOf("1", "2"), menuId, menuName)

        val expectedResult = Success(EditByMenu(menuId, menuName, listOf(), listOf()))

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
    fun `get total product count should return total product count from product list`() =
        runBlocking {
            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val expectedTotalProductCount = 10
            val productMeta = Meta(expectedTotalProductCount)

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData =
                ProductListData(ProductList(header = null, meta = productMeta, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val paramsProductList = createFilterOptions(1)
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId, filterOptions = paramsProductList)

            val actualProductCount = viewModel.getTotalProductCount()

            assertEquals(expectedTotalProductCount, actualProductCount)
        }

    @Test
    fun `given get product list is error when get total product count should return zero`() =
        runBlocking {
            onGetProductList_thenError(NullPointerException())

            viewModel.getProductList("10000")
            viewModel.getTotalProductCount()

            val expectedProductCount = 0
            val actualProductCount = viewModel.getTotalProductCount()

            assertEquals(expectedProductCount, actualProductCount)
        }

    @Test
    fun `given product list response meta null when get total product count should return zero`() =
        runBlocking {
            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData =
                ProductListData(ProductList(header = null, meta = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val paramsProductList = createFilterOptions(1)
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getProductList(shopId, filterOptions = paramsProductList)
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

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000),
                    pictures = pictures
                )
            )
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
    fun `when get featured product count and product list null should not set live data value`() {
        runBlocking {
            val shopId = "1500"

            val productListData = ProductListData(null)

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getFeaturedProductCount(shopId)

            verifyGetWarehouseIdCalled()
            verifyGetProductListCalled()
            assert(viewModel.productListFeaturedOnlyResult.value == null)
        }
    }

    @Test
    fun `when get featured product count and product list data null should not set live data value`() {
        runBlocking {
            val shopId = "1500"

            val productListData = ProductListData(ProductList(header = null, data = null))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)

            viewModel.getFeaturedProductCount(shopId)

            verifyGetWarehouseIdCalled()
            verifyGetProductListCalled()
            assert(viewModel.productListFeaturedOnlyResult.value == null)
        }
    }

    @Test
    fun `when get featured product count error, should set live data fail`() {
        runBlocking {
            val error = NullPointerException()
            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenError(error)

            viewModel.getFeaturedProductCount(anyString())

            viewModel.productListFeaturedOnlyResult.verifyErrorEquals(Fail(error))
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
            filterShownState = listOf(true, true, false, false)
        )

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
    fun `when edit variant price NOT success without error message should set live data value fail`() {
        val headerResponse = ProductUpdateV3Header(errorMessage = arrayListOf())
        val data = ProductUpdateV3Data(isSuccess = false, header = headerResponse)
        val response = ProductUpdateV3Response(data)
        val result = createEditVariantResult()

        onEditProductVariant_thenReturn(response)

        viewModel.editVariantsPrice(result)

        val expectedResult = Fail(MessageErrorException(""))

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
    fun `when edit variant stock success with variants not empty, set live data value success`() {
        runBlocking {
            val editStock = true
            val result = createEditVariantResult(
                editStock = editStock,
                variants = listOf(createProductVariant())
            )
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
    fun `when edit variant status error with empty error message should set live data value fail`() {
        val headerResponse = ProductUpdateV3Header(errorMessage = arrayListOf())
        val data = ProductUpdateV3Data(isSuccess = false, header = headerResponse)
        val response = ProductUpdateV3Response(data)
        val result = createEditVariantResult(editStatus = true)

        onEditProductVariant_thenReturn(response)

        viewModel.editVariantsStock(result)

        val expectedResult = Fail(MessageErrorException(""))

        viewModel.editVariantStockResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when edit variant status error without error message, should set live data value fail`() {
        runBlocking {
            val headerResponse = ProductUpdateV3Header()
            val data = ProductUpdateV3Data(isSuccess = false, header = headerResponse)
            val response = ProductUpdateV3Response(data)
            val result = createEditVariantResult(editStatus = true)

            onEditProductVariant_thenReturn(response)

            viewModel.editVariantsStock(result)

            assert(viewModel.editVariantStockResult.value is Fail)
        }
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
    fun `when get popups info success should set live data value success`() = runBlocking {
        val productId = "1"
        val showPopup = true

        onGetPopupsInfo_thenReturn(showPopup)

        viewModel.getPopupsInfo(productId)

        val expectedResult = Success(GetPopUpResult(productId, showPopup))

        viewModel.getPopUpResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get popups info success but false should set live data value success`() =
        runBlocking {
            val productId = "1"
            val showPopup = false

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
                productName = productName,
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
                createProductVariant(
                    name = "Biru | M",
                    combination = listOf(0, 1),
                    access = createDefaultAccess()
                ),
                createProductVariant(
                    name = "Hijau | S",
                    combination = listOf(1, 0),
                    access = createDefaultAccess()
                )
            )
            val expectedResult =
                GetVariantResult(productName, 0, productVariants, selections, emptyList())
            val expectedSuccessResult = Success(expectedResult)

            verifyGetVariantsCalled()

            viewModel.getProductVariantsResult.verifySuccessEquals(expectedSuccessResult)
        }
    }

    @Test
    fun `given get max stock success, when get variants success should set live data value success`() {
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
                productName = productName,
                products = variantList,
                selections = selections
            )

            val productId = "1400068494"
            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            val maxStock = 5

            onGetWarehouseId_thenReturn(locationList)
            onGetVariants_thenReturn(response)
            onGetMaxStockThreshold_thenReturn(maxStock)

            viewModel.getProductVariants(productId)

            val productVariants = listOf(
                createProductVariant(
                    name = "Biru | M",
                    combination = listOf(0, 1),
                    access = createDefaultAccess(),
                    maxStock = maxStock
                ),
                createProductVariant(
                    name = "Hijau | S",
                    combination = listOf(1, 0),
                    access = createDefaultAccess(),
                    maxStock = maxStock
                )
            )
            val expectedResult =
                GetVariantResult(productName, 0, productVariants, selections, emptyList())
            val expectedSuccessResult = Success(expectedResult)

            verifyGetVariantsCalled()

            viewModel.getProductVariantsResult.verifySuccessEquals(expectedSuccessResult)
        }
    }

    @Test
    fun `when get variants success but empty should not set live data value`() {
        runBlocking {
            val productName = "Tokopedia"
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
                products = listOf(),
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

            verifyGetVariantsCalled()
            assert(viewModel.getProductVariantsResult.value == null)
        }
    }

    @Test
    fun `when get product variants error should set live data fail`() {
        runBlocking {
            val error = NullPointerException()
            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )

            onGetWarehouseId_thenReturn(locationList)
            onGetVariants_thenError(error)

            viewModel.getProductVariants(anyString())

            val expectedResult = Fail(error)

            viewModel.getProductVariantsResult.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when get popups info error should set live data value fail`() = runBlocking {
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
            getShopManagerPopupsUseCase.cancelJobs()
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
            expectedProductManageAccess = createProductManageAccess(
                editProduct = true,
                editPrice = true
            )
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
            expectedProductManageAccess = createProductManageAccess(
                editProduct = true,
                editPrice = true
            )
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

        val expectedDeleteProductDialog =
            DeleteProductDialogType.MultipleProduct(isMultiLocationShop)

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
        viewModel.hideTicker()

        viewModel.showTicker
            .verifyValueEquals(false)
    }

    @Test
    fun `when getTickerData should set tickerData`() {
        runBlocking {
            val tickerData = listOf<TickerData>(mockk())

            val tickerResponse = GetTargetedTickerResponse(
                GetTargetedTickerResponse.GetTargetedTicker(
                    listOf(mockk())
                )
            )
            onGetIsMultiLocationShop_thenReturn(true)
            onGetTickerList_thenReturn(
                tickerResponse
            )
            onGetTickerData_thenReturn(tickerData, tickerResponse)

            val statusShop = StatusInfo("1", "", "", "")
            onGetStatusShop_thenReturn(statusShop)

            val accessData = Data(listOf(Access(ADD_PRODUCT)))
            val accessResponse = Response(data = accessData)

            onGetProductManageAccess_thenReturn(accessResponse)
            viewModel.getProductManageAccess()
            viewModel.getTickerData()

            verifyTickerDataEquals(tickerData)
        }
    }

    @Test
    fun `when getStatusShopUseCase is called should return shop status moderated`() {
        runBlocking {
            onGetIsShopOwner_thenReturn(isShopOwner = true)
            val statusShop = StatusInfo("3", "", "", "")
            onGetStatusShop_thenReturn(statusShop)
            viewModel.getProductManageAccess()
            viewModel.getTickerData()
            viewModel.shopStatus.verifyValueEquals(statusShop)
        }
    }

    @Test
    fun `when getStatusShopUseCase is called should return shop status moderated permanent`() {
        runBlocking {
            onGetIsShopOwner_thenReturn(isShopOwner = true)
            val statusShop = StatusInfo("5", "", "", "")
            onGetStatusShop_thenReturn(statusShop)
            viewModel.getProductManageAccess()
            viewModel.getTickerData()
            viewModel.shopStatus.verifyValueEquals(statusShop)
        }
    }

    @Test
    fun `given shop moderate data when getProductList more than once should set showStockTicker TRUE`() {
        runBlocking {
            val isMultiLocationShop = true

            val shopId = "1500"
            val pictures = listOf(Picture("imageUrl"))

            val paramsProductList = createFilterOptions(1)

            val productList = listOf(
                createProduct(
                    name = "Tolak Angin Madu",
                    price = Price(10000, 100000),
                    pictures = pictures
                )
            )
            val productListData = ProductListData(ProductList(header = null, data = productList))

            val locationList = listOf(
                ShopLocationResponse("1", MAIN_LOCATION),
                ShopLocationResponse("2", OTHER_LOCATION)
            )
            onGetWarehouseId_thenReturn(locationList)
            onGetProductList_thenReturn(productListData)
            onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)
            onGetIsShopOwner_thenReturn(isShopOwner = true)
            onGetTickerData_thenReturn(listOf(mockk()), mockk())
            val statusShop = StatusInfo("1", "", "", "")
            onGetStatusShop_thenReturn(statusShop)

            val accessData = Data(listOf(Access(ADD_PRODUCT)))
            val accessResponse = Response(data = accessData)

            onGetProductManageAccess_thenReturn(accessResponse)
            viewModel.getProductManageAccess()
            viewModel.getProductList(shopId, paramsProductList)
            viewModel.getTickerData()
            viewModel.showTicker()
            viewModel.shopStatus
                .verifyValueEquals(statusShop)
            viewModel.showTicker.verifyValueEquals(true)
        }
    }

    @Test
    fun `when getStatusShopUseCase is called should return shop status null`() {
        runBlocking {
            onGetIsShopOwner_thenReturn(isShopOwner = true)
            var statusShop = StatusInfo("", "", "", "")
            onGetStatusShop_thenReturn(statusShop)
            viewModel.getProductManageAccess()
            viewModel.getTickerData()
            viewModel.shopStatus.verifyValueEquals(statusShop)
        }
    }

    @Test
    fun `when getUploadStatusUseCase is called should return model data`() {
        val entity = UploadStatusEntity(
            id = 12,
            status = UploadStatusType.STATUS_DONE.name,
            productId = "12333"
        )

        val mockedRepository = MockedUploadStatusRepository(
            uploadStatusEntity = entity
        )

        getUploadStatusUseCase = GetUploadStatusUseCase(mockedRepository)

        // the getUploadStatusUseCase will be called after viewModel is initiated
        viewModel = ProductManageViewModel(
            editPriceUseCase,
            gqlGetShopInfoUseCase,
            getShopInfoTopAdsUseCase,
            userSessionInterface,
            getShopManagerPopupsUseCase,
            getProductListUseCase,
            setFeaturedProductUseCase,
            editStatusUseCase,
            editStockUseCase,
            deleteProductUseCase,
            multiEditProductUseCase,
            getProductListMetaUseCase,
            getProductManageAccessUseCase,
            editProductVariantUseCase,
            getProductVariantUseCase,
            getAdminInfoShopLocationUseCase,
            getUploadStatusUseCase,
            clearUploadStatusUseCase,
            getMaxStockThresholdUseCase,
            getStatusShopUseCase,
            getTickerUseCase,
            getShopWarehouse,
            productArchivalInfoUseCase,
            tickerStaticDataProvider,
            CoroutineTestDispatchersProvider
        )

        val model = entity.convertToModel()

        viewModel.uploadStatus
            .verifyValueEquals(model)
    }

    @Test
    fun `when clearUploadStatusUseCase is called should return null data`() {
        val entity = UploadStatusEntity(
            id = 12,
            status = UploadStatusType.STATUS_DONE.name,
            productId = "12333"
        )

        val mockedRepository = MockedUploadStatusRepository(
            uploadStatusEntity = entity
        )

        clearUploadStatusUseCase = ClearUploadStatusUseCase(mockedRepository)

        // the clearUploadStatusUseCase will be called after viewModel is initiated
        viewModel = ProductManageViewModel(
            editPriceUseCase,
            gqlGetShopInfoUseCase,
            getShopInfoTopAdsUseCase,
            userSessionInterface,
            getShopManagerPopupsUseCase,
            getProductListUseCase,
            setFeaturedProductUseCase,
            editStatusUseCase,
            editStockUseCase,
            deleteProductUseCase,
            multiEditProductUseCase,
            getProductListMetaUseCase,
            getProductManageAccessUseCase,
            editProductVariantUseCase,
            getProductVariantUseCase,
            getAdminInfoShopLocationUseCase,
            getUploadStatusUseCase,
            clearUploadStatusUseCase,
            getMaxStockThresholdUseCase,
            getStatusShopUseCase,
            getTickerUseCase,
            getShopWarehouse,
            productArchivalInfoUseCase,
            tickerStaticDataProvider,
            CoroutineTestDispatchersProvider
        )

        viewModel.clearUploadStatus()

        runBlocking {
            mockedRepository.flowEntity.collect {
                Assert.assertTrue(it == null)
            }
        }
    }

    @Test
    fun `when clearUploadStatusUseCase is called but get an error should do nothing`() {
        val mockedRepository = MockedUploadStatusRepositoryException()

        clearUploadStatusUseCase = ClearUploadStatusUseCase(mockedRepository)

        // the clearUploadStatusUseCase will be called after viewModel is initiated
        viewModel = ProductManageViewModel(
            editPriceUseCase,
            gqlGetShopInfoUseCase,
            getShopInfoTopAdsUseCase,
            userSessionInterface,
            getShopManagerPopupsUseCase,
            getProductListUseCase,
            setFeaturedProductUseCase,
            editStatusUseCase,
            editStockUseCase,
            deleteProductUseCase,
            multiEditProductUseCase,
            getProductListMetaUseCase,
            getProductManageAccessUseCase,
            editProductVariantUseCase,
            getProductVariantUseCase,
            getAdminInfoShopLocationUseCase,
            getUploadStatusUseCase,
            clearUploadStatusUseCase,
            getMaxStockThresholdUseCase,
            getStatusShopUseCase,
            getTickerUseCase,
            getShopWarehouse,
            productArchivalInfoUseCase,
            tickerStaticDataProvider,
            CoroutineTestDispatchersProvider
        )

        viewModel.clearUploadStatus()
    }

    @Test
    fun `when getProductArchivalInfo success, should set live data success`() {
        val successResponse = ProductArchivalInfo(ProductArchivalInfo.ProductarchivalGetProductArchiveInfo(
            "","","","",0
        ))
        coEvery {
            productArchivalInfoUseCase.execute(any())
        } returns successResponse

        viewModel.getProductArchivalInfo("")

        coVerify { productArchivalInfoUseCase.execute(any()) }
        assert(viewModel.productArchivalInfo.value == Success(successResponse))
    }

    @Test
    fun `when getProductArchivalInfo  error, should set live data fail`() {
        val throwable = NullPointerException()
        coEvery {
            productArchivalInfoUseCase.execute(any())
        } throws throwable

        viewModel.getProductArchivalInfo("")

        coVerify { productArchivalInfoUseCase.execute(any()) }
        assert(viewModel.productArchivalInfo.value is Fail)
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

    private fun onGetVariants_thenError(ex: Exception) {
        coEvery { getProductVariantUseCase.execute(any()) } throws ex
    }

    private fun verifyGetVariantsCalled() {
        coVerify { getProductVariantUseCase.execute(any()) }
    }

    private fun onShopWarehouse_thenReturn(response: ShopWarehouseResponse) {
        coEvery { getShopWarehouse.execute(any()) } returns response
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

    private suspend fun onEditPrice_thenError(ex: Exception) {
        coEvery { editPriceUseCase.executeOnBackground() } throws ex
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

    private suspend fun onDeleteProduct_thenError(error: Exception) {
        coEvery { deleteProductUseCase.executeOnBackground() } throws error
    }

    private suspend fun onSetFeaturedProduct_thenReturn(featuredProductResponseModel: FeaturedProductResponseModel) {
        coEvery { setFeaturedProductUseCase.executeOnBackground() } returns featuredProductResponseModel
    }

    private suspend fun onSetFeaturedProduct_thenError(ex: Exception) {
        coEvery { setFeaturedProductUseCase.executeOnBackground() } throws ex
    }

    private suspend fun onGetProductList_thenReturn(productListData: ProductListData) {
        coEvery { getProductListUseCase.execute(any()) } returns productListData
    }

    private suspend fun onGetTickerList_thenReturn(response: GetTargetedTickerResponse) {
        coEvery { getTickerUseCase.execute() } returns response
    }

    private suspend fun onGetStatusShop_thenReturn(statusInfo: StatusInfo) {
        coEvery {
            getStatusShopUseCase.params = GetStatusShopUseCase.createRequestParams(0)
            getStatusShopUseCase.executeOnBackground()
        } returns statusInfo
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

    private fun onGetPopupsInfo_thenReturn(showPopup: Boolean) {
        coEvery {
            getShopManagerPopupsUseCase.execute(any())
        } returns showPopup
    }

    private fun onGetPopupsInfo_thenReturn(error: Throwable) {
        coEvery {
            getShopManagerPopupsUseCase.execute(any())
        } throws error
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

    private fun onGetMaxStockThreshold_thenReturn(maxStock: Int?) {
        coEvery {
            getMaxStockThresholdUseCase.execute(any())
        } returns MaxStockThresholdResponse(
            getIMSMeta = MaxStockThresholdResponse.GetIMSMeta(
                data = MaxStockThresholdResponse.GetIMSMeta.Data(
                    maxStockThreshold = maxStock?.toString().orEmpty()
                ),
                header = com.tokopedia.network.data.model.response.Header()
            )
        )
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

    private fun onGetTickerData_thenReturn(
        tickerData: List<TickerData>,
        tickerResponse: GetTargetedTickerResponse
    ) {
        every {
            tickerStaticDataProvider.getTickers(
                true,
                "1",
                tickerResponse.getTargetedTicker?.tickers.orEmpty()
            )
        } returns tickerData
    }

    private fun onGetIsShowStockAvailableTicker(isShow: Boolean) {
        every {
            remoteConfigImpl.getBoolean(ENABLE_STOCK_AVAILABLE)
        } returns isShow
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
        val actualSetFeaturedProductResult =
            viewModel.setFeaturedProductResult.value as Success<SetFeaturedProductResult>
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
        viewModel.showTicker.verifyValueEquals(true)
    }

    private fun verifyTickerDataEquals(expected: List<TickerData>) {
        viewModel.tickerData.verifyValueEquals(expected)
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
