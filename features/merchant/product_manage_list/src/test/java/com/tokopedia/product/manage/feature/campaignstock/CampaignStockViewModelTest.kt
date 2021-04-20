package com.tokopedia.product.manage.feature.campaignstock

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse.*
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Data
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.data.createGetVariantResponse
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationSummary
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockData
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.NonVariantStockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.StockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.UpdateCampaignStockResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.VariantStockAllocationResult
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper
import com.tokopedia.product.manage.common.feature.variant.data.model.Product
import com.tokopedia.product.manage.common.feature.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.product.manage.data.createProductVariantResponse
import com.tokopedia.product.manage.data.createShopOwnerAccess
import com.tokopedia.product.manage.feature.campaignstock.CampaignStockViewModelTest.AccessId.EDIT_PRODUCT
import com.tokopedia.product.manage.feature.campaignstock.CampaignStockViewModelTest.AccessId.EDIT_STOCK
import com.tokopedia.product.manage.feature.campaignstock.CampaignStockViewModelTest.ProductStatusConstant.STATUS_CODE_ACTIVE
import com.tokopedia.product.manage.feature.campaignstock.CampaignStockViewModelTest.ProductStatusConstant.STATUS_CODE_INACTIVE
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationDetail
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationDetailSellable
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ProductStockWarehouse
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.ShopLocationResponse
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CampaignStockViewModelTest: CampaignStockViewModelTestFixture() {

    @Test
    fun `success get non variant stock allocation result`() = runBlocking {
        val productId = "1"
        val shopId = "1"
        val getStockAllocationData = GetStockAllocationData()
        val otherCampaignStockData = OtherCampaignStockData(status = ProductStatus.ACTIVE)

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.setShopId(shopId)
        viewModel.getStockAllocation(listOf(productId))

        verifyGetWarehouseIdCalled()
        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        val expectedResult = Success(NonVariantStockAllocationResult(
            getStockAllocationData,
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

        onGetCampaignStock_thenReturn(getStockAllocationData)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.setShopId(shopId)
        viewModel.getStockAllocation(listOf(productId))

        verifyGetCampaignStockAllocationCalled()
        verifyGetProductVariantCalled()
        verifyGetOtherCampaignStockDataCalled()

        val expectedResult = Success(VariantStockAllocationResult(
                ProductManageVariantMapper.mapToVariantsResult(getProductVariantResponse.getProductV3, createShopOwnerAccess()),
                getStockAllocationData,
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
        viewModel.getStockAllocation(listOf(productId))

        verifyGetCampaignStockAllocationCalled()

        viewModel.getStockAllocationData
            .verifyErrorEquals(Fail(NullPointerException()))
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
            getStockAllocation(listOf(productId))
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
                true
        ))

        verifyProductUpdateResponseResult(expectedResult)
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
            getStockAllocation(listOf(productId))
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
            getStockAllocation(listOf(productId))
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
                editVariantResponse.productUpdateV3Data.isSuccess,
                editVariantResponse.productUpdateV3Data.header.errorMessage.firstOrNull()
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
            getStockAllocation(listOf(productId))
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
            editVariantResponse.productUpdateV3Data.isSuccess,
            editVariantResponse.productUpdateV3Data.header.errorMessage.firstOrNull()
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
            getStockAllocation(listOf(productId))
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
            editVariantResponse.productUpdateV3Data.isSuccess,
            editVariantResponse.productUpdateV3Data.header.errorMessage.firstOrNull()
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
            getStockAllocation(listOf(productId))
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
            editVariantResponse.productUpdateV3Data.isSuccess,
            editVariantResponse.productUpdateV3Data.header.errorMessage.firstOrNull()
        ))
        verifyProductUpdateResponseResult(expectedResult)
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
        viewModel.getStockAllocation(listOf("100"))
        viewModel.toggleSaveButton(isMainTab)

        verifyGetProductVariantCalled()

        viewModel.showSaveBtn
            .verifyValueEquals(true)
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
        viewModel.getStockAllocation(listOf("100"))
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
        viewModel.getStockAllocation(listOf("100"))
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
        viewModel.getStockAllocation(listOf("100"))
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
        viewModel.getStockAllocation(listOf("100"))
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

        viewModel.getStockAllocation(productIds)

        verifyGetCampaignStockAllocationNotCalled()
    }

    private fun onGetCampaignStock_thenReturn(getStockAllocationData: GetStockAllocationData) {
        coEvery {
            campaignStockAllocationUseCase.executeOnBackground()
        } returns getStockAllocationData
    }

    private fun onGetCampaignStock_thenReturn(error: Throwable) {
        coEvery {
            campaignStockAllocationUseCase.executeOnBackground()
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

    private fun onGetProductManageAccess_thenReturn(response: Response) {
        coEvery {
            getProductManageAccessUseCase.execute(any())
        } returns response
    }

    private fun verifyGetCampaignStockAllocationCalled() {
        coVerify {
            campaignStockAllocationUseCase.executeOnBackground()
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