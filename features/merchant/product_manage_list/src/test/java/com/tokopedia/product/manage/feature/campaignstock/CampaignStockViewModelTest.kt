package com.tokopedia.product.manage.feature.campaignstock

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse.*
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Data
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Header
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.data.createGetVariantResponse
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationSummary
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockData
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper
import com.tokopedia.product.manage.common.feature.variant.data.model.Product
import com.tokopedia.product.manage.common.feature.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.product.manage.common.feature.variant.presentation.data.UpdateCampaignVariantResult
import com.tokopedia.product.manage.data.createProductVariantResponse
import com.tokopedia.product.manage.data.createShopOwnerAccess
import com.tokopedia.product.manage.feature.campaignstock.CampaignStockViewModelTest.AccessId.EDIT_PRODUCT
import com.tokopedia.product.manage.feature.campaignstock.CampaignStockViewModelTest.AccessId.EDIT_STOCK
import com.tokopedia.product.manage.feature.campaignstock.CampaignStockViewModelTest.ProductStatusConstant.STATUS_CODE_ACTIVE
import com.tokopedia.product.manage.feature.campaignstock.CampaignStockViewModelTest.ProductStatusConstant.STATUS_CODE_INACTIVE
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.CampaignData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationDetail
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationDetailReserve
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationDetailSellable
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.*
import com.tokopedia.product.manage.feature.campaignstock.ui.util.CampaignStockMapper
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ProductStockWarehouse
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ShopLocationResponse
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CampaignStockViewModelTest: CampaignStockViewModelTestFixture() {

    @Test
    fun `success get non variant stock allocation result`() = runBlocking {
        val productId = "1"
        val shopId = "1"
        val getStockAllocationData = GetStockAllocationData(
            detail = GetStockAllocationDetail(
                reserve = listOf(
                    GetStockAllocationDetailReserve()
                )
            )
        )
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.ACTIVE)

        val nonVariantReservedEventInfoUiModels = getStockAllocationData.detail.reserve.map {
            CampaignStockMapper.mapToParcellableReserved(it)
        } as ArrayList

        val access = createShopOwnerAccess()

        val sellableProducts = CampaignStockMapper.getSellableProduct(
            id = productId,
            isActive = otherCampaignStockData.getIsActive(),
            access = access,
            isCampaign = otherCampaignStockData.campaign?.isActive == true,
            maxStock = null,
            sellableList = getStockAllocationData.detail.sellable
        ) as ArrayList

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.setShopId(shopId)
        viewModel.getStockAllocation(listOf(productId), true)

        verifyGetWarehouseIdCalled()
        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        val expectedResult = Success(NonVariantStockAllocationResult(
            null,
            nonVariantReservedEventInfoUiModels,
            getStockAllocationData.summary,
            sellableProducts,
            otherCampaignStockData,
            createShopOwnerAccess()
        ))

        verifyGetStockAllocationSuccessResult(expectedResult)
    }

    @Test
    fun `success get non variant stock allocation result with active campaign`() = runBlocking {
        val productId = "1"
        val shopId = "1"
        val getStockAllocationData = GetStockAllocationData(
            detail = GetStockAllocationDetail(
                reserve = listOf(
                    GetStockAllocationDetailReserve()
                )
            )
        )
        val otherCampaignStockData = OtherCampaignStockData(
            status = ProductStatus.ACTIVE,
            campaign = CampaignData(true)
        )

        val nonVariantReservedEventInfoUiModels = getStockAllocationData.detail.reserve.map {
            CampaignStockMapper.mapToParcellableReserved(it)
        } as ArrayList

        val access = createShopOwnerAccess()

        val sellableProducts = CampaignStockMapper.getSellableProduct(
            id = productId,
            isActive = otherCampaignStockData.getIsActive(),
            access = access,
            isCampaign = otherCampaignStockData.campaign?.isActive == true,
            maxStock = null,
            sellableList = getStockAllocationData.detail.sellable
        ) as ArrayList

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.setShopId(shopId)
        viewModel.getStockAllocation(listOf(productId), true)

        verifyGetWarehouseIdCalled()
        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        val expectedResult = Success(NonVariantStockAllocationResult(
            null,
            nonVariantReservedEventInfoUiModels,
            getStockAllocationData.summary,
            sellableProducts,
            otherCampaignStockData,
            createShopOwnerAccess()
        ))

        verifyGetStockAllocationSuccessResult(expectedResult)
    }

    @Test
    fun `success get non variant stock allocation result with inactive campaign`() = runBlocking {
        val productId = "1"
        val shopId = "1"
        val getStockAllocationData = GetStockAllocationData(
            detail = GetStockAllocationDetail(
                reserve = listOf(
                    GetStockAllocationDetailReserve()
                )
            )
        )
        val otherCampaignStockData = OtherCampaignStockData(
            status = ProductStatus.ACTIVE,
            campaign = CampaignData(false)
        )

        val nonVariantReservedEventInfoUiModels = getStockAllocationData.detail.reserve.map {
            CampaignStockMapper.mapToParcellableReserved(it)
        } as ArrayList

        val access = createShopOwnerAccess()

        val sellableProducts = CampaignStockMapper.getSellableProduct(
            id = productId,
            isActive = otherCampaignStockData.getIsActive(),
            access = access,
            isCampaign = otherCampaignStockData.campaign?.isActive == true,
            maxStock = null,
            sellableList = getStockAllocationData.detail.sellable
        ) as ArrayList

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.setShopId(shopId)
        viewModel.getStockAllocation(listOf(productId), true)

        verifyGetWarehouseIdCalled()
        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        val expectedResult = Success(NonVariantStockAllocationResult(
            null,
            nonVariantReservedEventInfoUiModels,
            getStockAllocationData.summary,
            sellableProducts,
            otherCampaignStockData,
            createShopOwnerAccess()
        ))

        verifyGetStockAllocationSuccessResult(expectedResult)
    }

    @Test
    fun `success get variant stock allocation result`() = runBlocking {
        val productId = "1"
        val shopId = "1"
        val getStockAllocationData =
                GetStockAllocationData(
                        summary = GetStockAllocationSummary(
                                isVariant = true
                        )
                )
        val getProductVariantResponse = createGetVariantResponse()
        val otherCampaignStockData = OtherCampaignStockData()
        val getVariantResult = ProductManageVariantMapper.mapToVariantsResult(
            getProductVariantResponse.getProductV3,
            createShopOwnerAccess(),
            null
        )
        val sellableStockProductUiModels =
            CampaignStockMapper.mapToParcellableSellableProduct(
                getStockAllocationData.detail.sellable,
                getVariantResult.variants
            )
        val variantReservedEventInfoUiModels =
            CampaignStockMapper.mapToVariantReserved(getStockAllocationData.detail.reserve) as ArrayList

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.setShopId(shopId)
        viewModel.getStockAllocation(listOf(productId), false)

        verifyGetCampaignStockAllocationCalled()
        verifyGetProductVariantCalled()
        verifyGetOtherCampaignStockDataCalled()

        val expectedResult = Success(
            VariantStockAllocationResult(
                ProductManageVariantMapper.mapToVariantsResult(
                    getProductVariantResponse.getProductV3,
                    createShopOwnerAccess(),
                    null
                ),
                variantReservedEventInfoUiModels,
                getStockAllocationData.summary,
                sellableStockProductUiModels,
                otherCampaignStockData,
                createShopOwnerAccess()
            )
        )

        verifyGetStockAllocationSuccessResult(expectedResult)
    }

    @Test
    fun `given get max stock success, should set non variant result with max stock`() = runBlocking {
        val productId = "1"
        val shopId = "1"
        val maxStock = 5
        val getStockAllocationData = GetStockAllocationData()
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.ACTIVE)

        val nonVariantReservedEventInfoUiModels = getStockAllocationData.detail.reserve.map {
            CampaignStockMapper.mapToParcellableReserved(it)
        } as ArrayList

        val access = createShopOwnerAccess()

        val sellableProducts = CampaignStockMapper.getSellableProduct(
            id = productId,
            isActive = otherCampaignStockData.getIsActive(),
            access = access,
            isCampaign = otherCampaignStockData.campaign?.isActive == true,
            maxStock = null,
            sellableList = getStockAllocationData.detail.sellable
        ) as ArrayList


        onGetCampaignStock_thenReturn(getStockAllocationData)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetMaxStockThreshold_thenReturn(maxStock)

        viewModel.setShopId(shopId)
        viewModel.getStockAllocation(listOf(productId), true)

        verifyGetWarehouseIdCalled()
        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        val expectedResult = Success(NonVariantStockAllocationResult(
            maxStock,
            nonVariantReservedEventInfoUiModels,
            getStockAllocationData.summary,
            sellableProducts,
            otherCampaignStockData,
            access
        ))

        verifyGetStockAllocationSuccessResult(expectedResult)
    }

    @Test
    fun `given get max stock success, should set variant result with max stock`() = runBlocking {
        val productId = "1"
        val shopId = "1"
        val maxStock = 5
        val getStockAllocationData =
            GetStockAllocationData(
                summary = GetStockAllocationSummary(
                    isVariant = true
                )
            )
        val getProductVariantResponse = createGetVariantResponse()
        val otherCampaignStockData = OtherCampaignStockData()
        val getVariantResult = ProductManageVariantMapper.mapToVariantsResult(
            getProductVariantResponse.getProductV3,
            createShopOwnerAccess(),
            null
        )
        val sellableStockProductUiModels =
            CampaignStockMapper.mapToParcellableSellableProduct(
                getStockAllocationData.detail.sellable,
                getVariantResult.variants
            )
        val variantReservedEventInfoUiModels =
            CampaignStockMapper.mapToVariantReserved(getStockAllocationData.detail.reserve) as ArrayList

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetMaxStockThreshold_thenReturn(maxStock)

        viewModel.setShopId(shopId)
        viewModel.getStockAllocation(listOf(productId), false)

        verifyGetCampaignStockAllocationCalled()
        verifyGetProductVariantCalled()
        verifyGetOtherCampaignStockDataCalled()

        val expectedResult = Success(VariantStockAllocationResult(
            ProductManageVariantMapper.mapToVariantsResult(getProductVariantResponse.getProductV3, createShopOwnerAccess(), maxStock),
            variantReservedEventInfoUiModels,
            getStockAllocationData.summary,
            sellableStockProductUiModels,
            otherCampaignStockData,
            createShopOwnerAccess()))

        verifyGetStockAllocationSuccessResult(expectedResult)
    }

    @Test
    fun `given main location NOT found when getStockAllocation should set live data fail`() = runBlocking {
        val shopId = "1"
        val productId = "1"
        val locationList = listOf(
            ShopLocationResponse("2", LocationType.OTHER_LOCATION)
        )
        val error = NullPointerException()

        onGetWarehouseId_thenReturn(locationList)
        onGetCampaignStock_thenReturn(error)

        viewModel.setShopId(shopId)
        viewModel.getStockAllocation(listOf(productId), false)

        verifyGetCampaignStockAllocationCalled()

        viewModel.getStockAllocationData
            .verifyErrorEquals(Fail(error))
    }

    @Test
    fun `given edit status response ACTIVE when update non-variant stock should return status result ACTIVE`() = runBlocking {
        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val stockAllocationSummary = GetStockAllocationSummary(productName = productName, isVariant = false)
        val getStockAllocationData = GetStockAllocationData(summary = stockAllocationSummary)

        val editStatusResponse = ProductUpdateV3Response()
        val editStockResponse = ProductStockWarehouse(status = STATUS_CODE_ACTIVE)
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.INACTIVE)

        onEditStatus_thenReturn(editStatusResponse)
        onEditStock_thenReturn(editStockResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetCampaignStock_thenReturn(getStockAllocationData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        viewModel.run {
            updateNonVariantStockCount(1)
            updateNonVariantReservedStockCount(1)
            updateNonVariantIsActive(true)
            updateStockData()
        }

        verifyEditStatusCalled()
        verifyEditStockCalled()

        val totalStock = 2
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
            productName,
            totalStock,
            ProductStatus.ACTIVE,
            isStockChanged = true,
            isStatusChanged = true,
            true
        ))

        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `given edit status response is NULL when edit status NOT success should return initial product status`() = runBlocking {
        val isSuccess = false
        val productStatusResponse = null
        val initialProductStatus = ProductStatus.INACTIVE

        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val stockAllocationSummary = GetStockAllocationSummary(productName = productName, isVariant = false)
        val getStockAllocationData = GetStockAllocationData(summary = stockAllocationSummary)

        val editStatusResponse = ProductUpdateV3Response(ProductUpdateV3Data(isSuccess = isSuccess))
        val editStockResponse = ProductStockWarehouse(status = productStatusResponse)
        val otherCampaignStockData = OtherCampaignStockData(status = initialProductStatus)

        onEditStatus_thenReturn(editStatusResponse)
        onEditStock_thenReturn(editStockResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetCampaignStock_thenReturn(getStockAllocationData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        viewModel.run {
            updateNonVariantIsActive(true)
            updateNonVariantStockCount(1)
            updateStockData()
        }

        verifyEditStatusCalled()
        verifyEditStockCalled()

        val totalStock = 1
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
            productName,
            totalStock,
            initialProductStatus,
            isStockChanged = true,
            isStatusChanged = true,
            true
        ))

        verifyProductUpdateResponseResult(expectedResult)
    }


    @Test
    fun `given initial non variant status is same as updated status, should not get status`() = runBlocking {
        val isSuccess = false
        val productStatusResponse = null
        val initialProductStatus = ProductStatus.ACTIVE

        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val stockAllocationSummary = GetStockAllocationSummary(productName = productName, isVariant = false)
        val getStockAllocationData = GetStockAllocationData(summary = stockAllocationSummary)

        val editStatusResponse = ProductUpdateV3Response(ProductUpdateV3Data(isSuccess = isSuccess))
        val editStockResponse = ProductStockWarehouse(status = productStatusResponse)
        val otherCampaignStockData = OtherCampaignStockData(status = initialProductStatus)

        onEditStatus_thenReturn(editStatusResponse)
        onEditStock_thenReturn(editStockResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetCampaignStock_thenReturn(getStockAllocationData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        viewModel.run {
            updateNonVariantIsActive(true)
            updateNonVariantStockCount(1)
            updateStockData()
        }

        verifyEditStatusNotCalled()
        verifyEditStockCalled()

        val totalStock = 1
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
            productName,
            totalStock,
            initialProductStatus,
            isStockChanged = true,
            isStatusChanged = false,
            true
        ))

        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `given edit status response is NULL when edit status success should return product status input`() = runBlocking {
        val productStatusResponse = null
        val isSuccess = true
        val isActive = true // input = ProductStatus.ACTIVE

        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val stockAllocationSummary = GetStockAllocationSummary(productName = productName, isVariant = false)
        val getStockAllocationData = GetStockAllocationData(summary = stockAllocationSummary)

        val editStatusResponse = ProductUpdateV3Response(ProductUpdateV3Data(isSuccess = isSuccess))
        val editStockResponse = ProductStockWarehouse(status = productStatusResponse)
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.INACTIVE)

        onEditStatus_thenReturn(editStatusResponse)
        onEditStock_thenReturn(editStockResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetCampaignStock_thenReturn(getStockAllocationData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        viewModel.run {
            updateNonVariantIsActive(isActive)
            updateNonVariantStockCount(1)
            updateStockData()
        }

        verifyEditStatusCalled()
        verifyEditStockCalled()

        val totalStock = 1
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
            productName,
            totalStock,
            ProductStatus.ACTIVE,
            isStockChanged = true,
            isStatusChanged = true,
            true
        ))

        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `given edit product status is NOT success when updateStockData should return initial product status`() = runBlocking {
        val isSuccess = false
        val productStatus = ProductStatus.INACTIVE //initial status

        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val stockAllocationSummary = GetStockAllocationSummary(productName = productName, isVariant = false)
        val getStockAllocationData = GetStockAllocationData(summary = stockAllocationSummary)

        val editStatusResponse = ProductUpdateV3Response(ProductUpdateV3Data(isSuccess = isSuccess))
        val editStockResponse = ProductStockWarehouse(status = null)
        val otherCampaignStockData = OtherCampaignStockData(status = productStatus)

        onEditStatus_thenReturn(editStatusResponse)
        onEditStock_thenReturn(editStockResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetCampaignStock_thenReturn(getStockAllocationData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        viewModel.run {
            updateNonVariantIsActive(true)
            updateNonVariantStockCount(1)
            updateStockData()
        }

        verifyEditStatusCalled()
        verifyEditStockCalled()

        val totalStock = 1
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
            productName,
            totalStock,
            ProductStatus.INACTIVE,
            isStockChanged = true,
            isStatusChanged = true,
            true
        ))

        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `given edit product status is NOT success with error message, when updateStockData should return result with error message`() = runBlocking {
        val isSuccess = false
        val productStatus = ProductStatus.INACTIVE //initial status

        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val errorMessage = "This is error :("

        val stockAllocationSummary = GetStockAllocationSummary(productName = productName, isVariant = false)
        val getStockAllocationData = GetStockAllocationData(summary = stockAllocationSummary)

        val editStatusResponse = ProductUpdateV3Response(ProductUpdateV3Data(
            header = ProductUpdateV3Header(
                errorMessage = arrayListOf(errorMessage)
            ),
            isSuccess = isSuccess))
        val editStockResponse = ProductStockWarehouse(status = null)
        val otherCampaignStockData = OtherCampaignStockData(status = productStatus)

        onEditStatus_thenReturn(editStatusResponse)
        onEditStock_thenReturn(editStockResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetCampaignStock_thenReturn(getStockAllocationData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        viewModel.run {
            updateNonVariantIsActive(true)
            updateNonVariantStockCount(1)
            updateStockData()
        }

        verifyEditStatusCalled()
        verifyEditStockCalled()

        val totalStock = 1
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
            productName,
            totalStock,
            ProductStatus.INACTIVE,
            true,
            true,
            true
        ))

        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `given non variant status and stock NOT changed, when updateStockData, should not set live data value`() = runBlocking {
        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val stockAllocationSummary = GetStockAllocationSummary(
            productName = productName,
            sellableStock = "1",
            isVariant = false)
        val getStockAllocationData = GetStockAllocationData(summary = stockAllocationSummary)

        val editStatusResponse = ProductUpdateV3Response()
        val editStockResponse = ProductStockWarehouse(status = STATUS_CODE_ACTIVE)
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.INACTIVE)

        onEditStatus_thenReturn(editStatusResponse)
        onEditStock_thenReturn(editStockResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetCampaignStock_thenReturn(getStockAllocationData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        viewModel.run {
            updateNonVariantStockCount(1)
            updateNonVariantReservedStockCount(1)
            updateNonVariantIsActive(false)
            updateStockData()
        }

        verifyEditStatusNotCalled()
        verifyEditStockNotCalled()
    }

    @Test
    fun `given update non variant status error, should set live data value fail`() = runBlocking {
        val isSuccess = true
        val productStatus = ProductStatus.INACTIVE //initial status

        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val stockAllocationSummary = GetStockAllocationSummary(productName = productName, isVariant = false)
        val getStockAllocationData = GetStockAllocationData(summary = stockAllocationSummary)

        val editStatusResponse = ProductUpdateV3Response(ProductUpdateV3Data(isSuccess = isSuccess))
        val editStockResponse = ProductStockWarehouse(status = null)
        val otherCampaignStockData = OtherCampaignStockData(status = productStatus)

        val error = NullPointerException()

        onEditStatus_thenReturn(editStatusResponse)
        onEditStock_thenReturn(editStockResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditStatus_thenThrow(error)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        viewModel.run {
            updateNonVariantIsActive(true)
            updateNonVariantStockCount(1)
            updateStockData()
        }

        verifyEditStatusCalled()
        assert(viewModel.productUpdateResponseLiveData.value is Fail)
    }

    @Test
    fun `given edit status response INACTIVE when update non-variant stock should return status result INACTIVE`() = runBlocking {
        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val stockAllocationSummary = GetStockAllocationSummary(productName = productName, isVariant = false)
        val getStockAllocationData = GetStockAllocationData(summary = stockAllocationSummary)

        val editStatusResponse = ProductUpdateV3Response()
        val editStockResponse = ProductStockWarehouse(status = STATUS_CODE_INACTIVE)
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.ACTIVE)

        onEditStatus_thenReturn(editStatusResponse)
        onEditStock_thenReturn(editStockResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetCampaignStock_thenReturn(getStockAllocationData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        viewModel.run {
            updateNonVariantStockCount(1)
            updateNonVariantReservedStockCount(1)
            updateNonVariantIsActive(false)
            updateStockData()
        }

        verifyEditStatusCalled()
        verifyEditStockCalled()

        val totalStock = 2
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
            productName,
            totalStock,
            ProductStatus.INACTIVE,
            isStockChanged = true,
            isStatusChanged = true,
            true
        ))

        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `success update variant stock data`() = runBlocking {
        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val getStockAllocationData =
                GetStockAllocationData(
                        summary = GetStockAllocationSummary(
                            productName = productName,
                            isVariant = true,
                            reserveStock = "1"
                        )
                )
        val productUpdateV3Data = ProductUpdateV3Data(isSuccess = true)
        val editVariantResponse = ProductUpdateV3Response(productUpdateV3Data)
        val getProductVariantResponse = createGetVariantResponse(
            productName = productName,
            products = listOf(createProductVariantResponse(productID = productId))
        )
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.INACTIVE)

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditVariant_thenReturn(editVariantResponse)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), true)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetProductVariantCalled()

        viewModel.run {
            updateVariantStockCount(productId, 2)
            updateVariantIsActive(productId, ProductStatus.INACTIVE)
            updateStockData()
        }

        verifyEditProductVariantCalled()

        val totalStock = 3
        val expectedResult = Success(UpdateCampaignStockResult(
                productId,
                productName,
                totalStock,
                ProductStatus.INACTIVE,
                isStockChanged = true,
                isStatusChanged = true,
                editVariantResponse.productUpdateV3Data.isSuccess,
                editVariantResponse.productUpdateV3Data.header.errorMessage.firstOrNull(),
                hashMapOf(
                        Pair(productId, UpdateCampaignVariantResult(ProductStatus.INACTIVE, 2, ""))
                )
        ))
        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `given variant is empty when update stock data should NOT update variant stock data`() = runBlocking {
        val variantList = listOf<Product>()

        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val getStockAllocationData = GetStockAllocationData(
            summary = GetStockAllocationSummary(
                productName = productName,
                isVariant = true,
                reserveStock = "1"
            ))
        val productUpdateV3Data = ProductUpdateV3Data(isSuccess = true)
        val editVariantResponse = ProductUpdateV3Response(productUpdateV3Data)
        val getProductVariantResponse = createGetVariantResponse(
            productName = productName,
            products = variantList
        )
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.ACTIVE)

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditVariant_thenReturn(editVariantResponse)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetProductVariantCalled()

        viewModel.run {
            updateVariantStockCount(productId, 1)
            updateVariantIsActive(productId, ProductStatus.ACTIVE)
            updateStockData()
        }

        verifyEditStatusNotCalled()
        verifyEditStockNotCalled()

        val totalStock = 1
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
            productName,
            totalStock,
            ProductStatus.INACTIVE,
            isStockChanged = false,
            isStatusChanged = false,
            editVariantResponse.productUpdateV3Data.isSuccess,
            editVariantResponse.productUpdateV3Data.header.errorMessage.firstOrNull(),
            hashMapOf()
        ))
        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `given one of variant not found when updateStockData should update result success`() = runBlocking {
        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val getStockAllocationData = GetStockAllocationData(
            summary = GetStockAllocationSummary(
                productName = productName,
                isVariant = true,
                reserveStock = "1"
            )
        )
        val productUpdateV3Data = ProductUpdateV3Data(isSuccess = true)
        val editVariantResponse = ProductUpdateV3Response(productUpdateV3Data)
        val getProductVariantResponse = createGetVariantResponse(
            productName = productName,
            products = listOf(
                createProductVariantResponse(productID = "1", status = ProductStatus.ACTIVE),
                createProductVariantResponse(productID = "2", status = ProductStatus.INACTIVE)
            )
        )
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.ACTIVE)

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditVariant_thenReturn(editVariantResponse)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetProductVariantCalled()

        viewModel.run {
            updateVariantStockCount("1", 1)
            updateVariantStockCount("3", 1)
            updateVariantIsActive("1", ProductStatus.INACTIVE)
            updateVariantIsActive("3", ProductStatus.ACTIVE)
            updateStockData()
        }

        verifyEditProductVariantCalled()

        val totalStock = 2
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
            productName,
            totalStock,
            ProductStatus.INACTIVE,
            isStockChanged = true,
            isStatusChanged = true,
            editVariantResponse.productUpdateV3Data.isSuccess,
            editVariantResponse.productUpdateV3Data.header.errorMessage.firstOrNull(),
            hashMapOf(
                    Pair("1", UpdateCampaignVariantResult(ProductStatus.INACTIVE, 1, "")),
                    Pair("2", UpdateCampaignVariantResult(ProductStatus.INACTIVE, 0, ""))
            )
        ))
        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `given variant not changed when updateStockData should NOT call edit stock and status`() = runBlocking {
        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val getStockAllocationData = GetStockAllocationData(
            summary = GetStockAllocationSummary(
                productName = productName,
                isVariant = true,
                reserveStock = "1"
            ),
            detail = GetStockAllocationDetail(
                sellable = listOf(
                    GetStockAllocationDetailSellable(productId = "1", stock = "1"),
                    GetStockAllocationDetailSellable(productId = "2", stock = "2")
                )
            )
        )
        val productUpdateV3Data = ProductUpdateV3Data(isSuccess = true)
        val editVariantResponse = ProductUpdateV3Response(productUpdateV3Data)
        val getProductVariantResponse = createGetVariantResponse(
            productName = productName,
            products = listOf(
                createProductVariantResponse(productID = "1", stock = 3, status = ProductStatus.ACTIVE),
                createProductVariantResponse(productID = "2", stock = 5, status = ProductStatus.INACTIVE)
            )
        )
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.ACTIVE)
        val locationList = listOf(
            ShopLocationResponse("1", MAIN_LOCATION),
            ShopLocationResponse("2", OTHER_LOCATION)
        )

        onGetWarehouseId_thenReturn(locationList)
        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditVariant_thenReturn(editVariantResponse)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetProductVariantCalled()
        verifyGetWarehouseIdCalled()

        viewModel.run {
            updateVariantStockCount("1", 1)
            updateVariantStockCount("2", 2)
            updateVariantIsActive("1", ProductStatus.ACTIVE)
            updateVariantIsActive("2", ProductStatus.INACTIVE)
            updateStockData()
        }

        verifyEditStatusNotCalled()
        verifyEditStockNotCalled()

        val totalStock = 4
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
            productName,
            totalStock,
            ProductStatus.ACTIVE,
            isStockChanged = false,
            isStatusChanged = false,
            editVariantResponse.productUpdateV3Data.isSuccess,
            editVariantResponse.productUpdateV3Data.header.errorMessage.firstOrNull(),
            hashMapOf(
                    Pair("1", UpdateCampaignVariantResult(ProductStatus.ACTIVE, 1, "")),
                    Pair("2", UpdateCampaignVariantResult(ProductStatus.INACTIVE, 2, ""))
            )
        ))
        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `given variant status not changed but stock changed when updateStockData should NOT call edit status`() = runBlocking {
        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val getStockAllocationData = GetStockAllocationData(
            summary = GetStockAllocationSummary(
                productName = productName,
                isVariant = true,
                reserveStock = "1"
            ),
            detail = GetStockAllocationDetail(
                sellable = listOf(
                    GetStockAllocationDetailSellable(productId = "1", stock = "1"),
                    GetStockAllocationDetailSellable(productId = "2", stock = "2")
                )
            )
        )
        val productUpdateV3Data = ProductUpdateV3Data(isSuccess = true)
        val editVariantResponse = ProductUpdateV3Response(productUpdateV3Data)
        val getProductVariantResponse = createGetVariantResponse(
            productName = productName,
            products = listOf(
                createProductVariantResponse(productID = "1", stock = 3, status = ProductStatus.ACTIVE),
                createProductVariantResponse(productID = "2", stock = 5, status = ProductStatus.INACTIVE)
            )
        )
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.ACTIVE)
        val locationList = listOf(
            ShopLocationResponse("1", MAIN_LOCATION),
            ShopLocationResponse("2", OTHER_LOCATION)
        )

        onGetWarehouseId_thenReturn(locationList)
        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditVariant_thenReturn(editVariantResponse)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetProductVariantCalled()
        verifyGetWarehouseIdCalled()

        viewModel.run {
            updateVariantStockCount("1", 1)
            updateVariantStockCount("2", 3)
            updateVariantIsActive("1", ProductStatus.ACTIVE)
            updateVariantIsActive("2", ProductStatus.INACTIVE)
            updateStockData()
        }

        verifyEditStatusNotCalled()
        verifyEditStockCalled()

        val totalStock = 5
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
            productName,
            totalStock,
            ProductStatus.ACTIVE,
            isStockChanged = true,
            isStatusChanged = false,
            editVariantResponse.productUpdateV3Data.isSuccess,
            editVariantResponse.productUpdateV3Data.header.errorMessage.firstOrNull(),
            hashMapOf(
                Pair("1", UpdateCampaignVariantResult(ProductStatus.ACTIVE, 1, "")),
                Pair("2", UpdateCampaignVariantResult(ProductStatus.INACTIVE, 3, ""))
            )
        ))
        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `given update variant stock data error, should set live data value to Fail`() = runBlocking {
        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val getStockAllocationData =
            GetStockAllocationData(
                summary = GetStockAllocationSummary(
                    productName = productName,
                    isVariant = true,
                    reserveStock = "1"
                )
            )
        val getProductVariantResponse = createGetVariantResponse(
            productName = productName,
            products = listOf(createProductVariantResponse(productID = productId))
        )
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.INACTIVE)

        val error = NullPointerException()

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditVariant_thenThrow(error)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), true)
        }

        verifyGetCampaignStockAllocationCalled()
        verifyGetProductVariantCalled()

        viewModel.run {
            updateVariantStockCount(productId, 2)
            updateVariantIsActive(productId, ProductStatus.INACTIVE)
            updateStockData()
        }

        verifyEditProductVariantCalled()

        assert(viewModel.productUpdateResponseLiveData.value is Fail)
    }

    @Test
    fun `given shop admin has edit stock or product access when toggleSaveBtn on mainTab should set mShowSaveBtn to true`() {
        val isShopOwner = false
        val isMainTab = true

        val accessData = Data(listOf(Access(EDIT_STOCK), Access(EDIT_PRODUCT)))
        val accessResponse = Response(data = accessData)

        val getStockAllocationData =
            GetStockAllocationData(
                summary = GetStockAllocationSummary(
                    isVariant = true
                )
            )
        val editVariantResponse = ProductUpdateV3Response()
        val getProductVariantResponse = createGetVariantResponse()
        val otherCampaignStockData = OtherCampaignStockData()

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditVariant_thenReturn(editVariantResponse)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        onGetIsShopOwner_thenReturn(isShopOwner)
        onGetProductManageAccess_thenReturn(accessResponse)

        viewModel.setShopId("1")
        viewModel.getStockAllocation(listOf("100"), false)
        viewModel.toggleSaveButton(isMainTab)

        verifyGetProductVariantCalled()

        viewModel.showSaveBtn
            .verifyValueEquals(true)
    }

    @Test
    fun `given is not shop owner, when getStockAllocation non variant, should get access`() {
        val isShopOwner = false

        val accessData = Data(listOf(Access(EDIT_STOCK), Access(EDIT_PRODUCT)))
        val accessResponse = Response(data = accessData)

        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val stockAllocationSummary = GetStockAllocationSummary(productName = productName, isVariant = false)
        val getStockAllocationData = GetStockAllocationData(summary = stockAllocationSummary)

        val editStatusResponse = ProductUpdateV3Response()
        val editStockResponse = ProductStockWarehouse(status = STATUS_CODE_ACTIVE)
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.INACTIVE)

        onEditStatus_thenReturn(editStatusResponse)
        onEditStock_thenReturn(editStockResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onGetCampaignStock_thenReturn(getStockAllocationData)
        onGetIsShopOwner_thenReturn(isShopOwner)
        onGetProductManageAccess_thenReturn(accessResponse)

        viewModel.run {
            setShopId(shopId)
            getStockAllocation(listOf(productId), false)
        }

        verifyGetProductManageAccessCalled()
    }

    @Test
    fun `given shop admin has edit product access when toggleSaveBtn on campaign tab should set mShowSaveBtn to false`() {
        val isShopOwner = false
        val isMainTab = false

        val accessData = Data(listOf(Access(EDIT_PRODUCT)))
        val accessResponse = Response(data = accessData)

        val getStockAllocationData =
            GetStockAllocationData(
                summary = GetStockAllocationSummary(
                    isVariant = true
                )
            )
        val editVariantResponse = ProductUpdateV3Response()
        val getProductVariantResponse = createGetVariantResponse()
        val otherCampaignStockData = OtherCampaignStockData()

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditVariant_thenReturn(editVariantResponse)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        onGetIsShopOwner_thenReturn(isShopOwner)
        onGetProductManageAccess_thenReturn(accessResponse)

        viewModel.setShopId("1")
        viewModel.getStockAllocation(listOf("100"), false)
        viewModel.toggleSaveButton(isMainTab)

        verifyGetProductVariantCalled()

        viewModel.showSaveBtn
            .verifyValueEquals(false)
    }

    @Test
    fun `given shop admin does NOT have edit stock and product access when toggleSaveBtn should set mShowSaveBtn to false`() {
        val isShopOwner = false
        val isMainTab = true

        val accessData = Data(listOf())
        val accessResponse = Response(data = accessData)

        val getStockAllocationData =
            GetStockAllocationData(
                summary = GetStockAllocationSummary(
                    isVariant = true
                )
            )
        val editVariantResponse = ProductUpdateV3Response()
        val getProductVariantResponse = createGetVariantResponse(
            products = listOf(createProductVariantResponse())
        )
        val otherCampaignStockData = OtherCampaignStockData()

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditVariant_thenReturn(editVariantResponse)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        onGetIsShopOwner_thenReturn(isShopOwner)
        onGetProductManageAccess_thenReturn(accessResponse)

        viewModel.setShopId("1")
        viewModel.getStockAllocation(listOf("100"), false)
        viewModel.toggleSaveButton(isMainTab)

        verifyGetProductVariantCalled()

        viewModel.showSaveBtn
            .verifyValueEquals(false)
    }

    @Test
    fun `given user is shop owner when toggleSaveBtn on mainTab should set mShowSaveBtn to true`() {
        val isShopOwner = true
        val isMainTab = true

        val getStockAllocationData =
            GetStockAllocationData(
                summary = GetStockAllocationSummary(
                    isVariant = true
                )
            )
        val editVariantResponse = ProductUpdateV3Response()
        val getProductVariantResponse = createGetVariantResponse()
        val otherCampaignStockData = OtherCampaignStockData()

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditVariant_thenReturn(editVariantResponse)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        onGetIsShopOwner_thenReturn(isShopOwner)

        viewModel.setShopId("1")
        viewModel.getStockAllocation(listOf("100"), false)
        viewModel.toggleSaveButton(isMainTab)

        verifyGetProductVariantCalled()

        viewModel.showSaveBtn
            .verifyValueEquals(true)
    }

    @Test
    fun `given user is shop owner when toggleSaveBtn on campaign tab should set mShowSaveBtn to false`() {
        val isShopOwner = true
        val isMainTab = false

        val getStockAllocationData =
            GetStockAllocationData(
                summary = GetStockAllocationSummary(
                    isVariant = true
                )
            )
        val editVariantResponse = ProductUpdateV3Response()
        val getProductVariantResponse = createGetVariantResponse()
        val otherCampaignStockData = OtherCampaignStockData()

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onEditVariant_thenReturn(editVariantResponse)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        onGetIsShopOwner_thenReturn(isShopOwner)

        viewModel.setShopId("1")
        viewModel.getStockAllocation(listOf("100"), false)
        viewModel.toggleSaveButton(isMainTab)

        verifyGetProductVariantCalled()

        viewModel.showSaveBtn
            .verifyValueEquals(false)
    }

    @Test
    fun `given product manage access NULL when toggleSaveButton should set showSaveBtn false`() {
        val isMainTab = true

        viewModel.toggleSaveButton(isMainTab)

        viewModel.showSaveBtn
            .verifyValueEquals(false)
    }

    @Test
    fun `when productIds is empty should NOT call campaignStockAllocationUseCase`() {
        val productIds = listOf<String>()

        viewModel.getStockAllocation(productIds, false)

        verifyGetCampaignStockAllocationNotCalled()
    }

    @Test
    fun `given non-empty ticker data when multilocation TRUE than show ticker `() {
        val isMultiLocationShop = true
        onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)
        onGetTickerData_thenReturn(listOf(mockk()))

        viewModel.getTickerData()
        val actualResult = viewModel.tickerData.value
        assert(!actualResult.isNullOrEmpty())
    }

    @Test
    fun `given empty ticker data when multilocation FALSE than don't show ticker `() {
        val isMultiLocationShop = false
        onGetIsMultiLocationShop_thenReturn(isMultiLocationShop)
        onGetTickerData_thenReturn(listOf())

        viewModel.getTickerData()
        val actualResult = viewModel.tickerData.value

        assert(actualResult.isNullOrEmpty())
    }

    private fun onGetCampaignStock_thenReturn(getStockAllocationData: GetStockAllocationData) {
        coEvery {
            campaignStockAllocationUseCase.execute(any(), any(), any(), any())
        } returns getStockAllocationData
    }

    private fun onGetTickerData_thenReturn(tickerData: List<TickerData>) {
        every {
            tickerStaticDataProvider.getTickers(any())
        } returns tickerData
    }

    private fun onGetCampaignStock_thenReturn(error: Throwable) {
        coEvery {
            campaignStockAllocationUseCase.execute(any(), any(), any(), any())
        } throws error
    }

    private fun onGetOtherCampaignStock_thenReturn(otherCampaignStockData: OtherCampaignStockData) {
        coEvery {
            otherCampaignStockDataUseCase.executeOnBackground()
        } returns otherCampaignStockData
    }

    private fun onGetProductVariant_thenReturn(getProductVariantResponse: GetProductVariantResponse) {
        coEvery {
            getProductVariantUseCase.execute(any())
        } returns getProductVariantResponse
    }

    private fun onEditStatus_thenReturn(response: ProductUpdateV3Response) {
        coEvery {
            editStatusUseCase.executeOnBackground()
        } returns response
    }

    private fun onGetIsMultiLocationShop_thenReturn(isMultiLocationShop: Boolean) {
        coEvery {
            userSession.isMultiLocationShop
        } returns isMultiLocationShop
    }

    private fun onEditStatus_thenThrow(ex: Exception) {
        coEvery {
            editStatusUseCase.executeOnBackground()
        } throws ex
    }

    private fun onEditStock_thenReturn(response: ProductStockWarehouse) {
        coEvery {
            editStockUseCase.execute(any())
        } returns response
    }

    private fun onEditVariant_thenReturn(productUpdateV3Response: ProductUpdateV3Response) {
        coEvery {
            editProductVariantUseCase.execute(any())
        } returns productUpdateV3Response
    }

    private fun onEditVariant_thenThrow(ex: Exception) {
        coEvery {
            editProductVariantUseCase.execute(any())
        } throws ex
    }

    private fun onGetProductManageAccess_thenReturn(response: Response) {
        coEvery {
            getProductManageAccessUseCase.execute(any())
        } returns response
    }

    private fun verifyGetCampaignStockAllocationCalled() {
        coVerify {
            campaignStockAllocationUseCase.execute(any(), any(), any(), any())
        }
    }

    private fun verifyGetCampaignStockAllocationNotCalled() {
        coVerify(exactly = 0) {
            campaignStockAllocationUseCase.executeOnBackground()
        }
    }

    private fun verifyEditStatusCalled() {
        coVerify {
            editStatusUseCase.executeOnBackground()
        }
    }

    private fun verifyEditStatusNotCalled() {
        coVerify(exactly = 0) {
            editStatusUseCase.executeOnBackground()
        }
    }

    private fun verifyEditStockCalled() {
        coVerify {
            editStockUseCase.execute(any())
        }
    }

    private fun verifyEditStockNotCalled() {
        coVerify(exactly = 0) {
            editStockUseCase.execute(any())
        }
    }

    private fun verifyEditProductVariantCalled() {
        coVerify {
            editProductVariantUseCase.execute(any())
        }
    }

    private fun verifyGetOtherCampaignStockDataCalled() {
        coVerify {
            otherCampaignStockDataUseCase.executeOnBackground()
        }
    }

    private fun verifyGetProductVariantCalled() {
        coVerify {
            getProductVariantUseCase.execute(any())
        }
    }

    private fun verifyGetWarehouseIdCalled() {
        coVerify {
            getAdminInfoShopLocationUseCase.execute(any())
        }
    }

    private fun verifyGetProductManageAccessCalled() {
        coVerify {
            getProductManageAccessUseCase.execute(any())
        }
    }

    private fun verifyGetStockAllocationSuccessResult(expectedResult: Success<StockAllocationResult>) {
        val actualResult = viewModel.getStockAllocationData.value
        assertEquals(expectedResult, actualResult)
    }

    private fun verifyProductUpdateResponseResult(expectedResult: Success<UpdateCampaignStockResult>) {
        val actualResult = viewModel.productUpdateResponseLiveData.value
        assertEquals(expectedResult, actualResult)
    }

    private object AccessId {
        const val EDIT_PRODUCT = "121"
        const val EDIT_STOCK = "124"
    }

    private object ProductStatusConstant {
        const val STATUS_CODE_ACTIVE = 1
        const val STATUS_CODE_INACTIVE = 3
    }
}
