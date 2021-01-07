package com.tokopedia.product.manage.feature.campaignstock

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse.*
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
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unit.test.ext.verifyValueEquals
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
        val otherCampaignStockData = OtherCampaignStockData()

        onSetCampaignStock_thenReturn(getStockAllocationData)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.getStockAllocation(listOf(productId))
        viewModel.setShopId(shopId)

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

        onSetCampaignStock_thenReturn(getStockAllocationData)
        onGetProductVariant_thenReturn(getProductVariantResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.getStockAllocation(listOf(productId))
        viewModel.setShopId(shopId)

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
    fun `success update non-variant stock data`() = runBlocking {
        val shopId = "1"
        val productId = "1"
        val productName = "Name"

        val stockAllocationSummary = GetStockAllocationSummary(productName = productName, isVariant = false)
        val getStockAllocationData = GetStockAllocationData(summary = stockAllocationSummary)

        val editStockResponse = ProductUpdateV3Response()
        val otherCampaignStockData = OtherCampaignStockData()

        onEditStock_thenReturn(editStockResponse)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)
        onSetCampaignStock_thenReturn(getStockAllocationData)

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

        verifyEditStockCalled()

        val totalStock = 2
        val expectedResult = Success(UpdateCampaignStockResult(
            productId,
                productName,
                totalStock,
                ProductStatus.INACTIVE,
                editStockResponse.productUpdateV3Data.isSuccess,
                editStockResponse.productUpdateV3Data.header.errorMessage.firstOrNull()
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
        val editVariantResponse = ProductUpdateV3Response()
        val getProductVariantResponse = createGetVariantResponse(
            productName = productName,
            products = listOf(createProductVariantResponse(productID = productId))
        )
        val otherCampaignStockData = OtherCampaignStockData()

        onSetCampaignStock_thenReturn(getStockAllocationData)
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
        val editVariantResponse = ProductUpdateV3Response()
        val getProductVariantResponse = createGetVariantResponse(
            productName = productName,
            products = variantList
        )
        val otherCampaignStockData = OtherCampaignStockData()

        onSetCampaignStock_thenReturn(getStockAllocationData)
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

        verifyEditProductVariantCalled()

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
        val editVariantResponse = ProductUpdateV3Response()
        val getProductVariantResponse = createGetVariantResponse(
            productName = productName,
            products = listOf(
                createProductVariantResponse(productID = "1", status = ProductStatus.ACTIVE),
                createProductVariantResponse(productID = "2", status = ProductStatus.INACTIVE)
            )
        )
        val otherCampaignStockData = OtherCampaignStockData()

        onSetCampaignStock_thenReturn(getStockAllocationData)
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

        onSetCampaignStock_thenReturn(getStockAllocationData)
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

        onSetCampaignStock_thenReturn(getStockAllocationData)
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

        onSetCampaignStock_thenReturn(getStockAllocationData)
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

        onSetCampaignStock_thenReturn(getStockAllocationData)
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

        onSetCampaignStock_thenReturn(getStockAllocationData)
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

    private fun onSetCampaignStock_thenReturn(getStockAllocationData: GetStockAllocationData) {
        coEvery {
            campaignStockAllocationUseCase.executeOnBackground()
        } returns getStockAllocationData
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

    private fun onEditStock_thenReturn(productUpdateV3Response: ProductUpdateV3Response) {
        coEvery {
            editStockUseCase.executeOnBackground()
        } returns productUpdateV3Response
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

    private fun verifyEditStockCalled() {
        coVerify {
            editStockUseCase.executeOnBackground()
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
}