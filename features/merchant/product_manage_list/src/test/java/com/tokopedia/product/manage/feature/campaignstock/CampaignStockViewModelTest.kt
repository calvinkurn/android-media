package com.tokopedia.product.manage.feature.campaignstock

import com.tokopedia.product.manage.data.createGetVariantResponse
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationSummary
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockData
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.NonVariantStockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.StockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.UpdateCampaignStockResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.VariantStockAllocationResult
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.feature.quickedit.variant.data.mapper.ProductManageVariantMapper
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.*

class CampaignStockViewModelTest: CampaignStockViewModelTestFixture() {

    @Test
    fun `success get non variant stock allocation result`() = runBlocking {
        val getStockAllocationData = GetStockAllocationData()
        val otherCampaignStockData = OtherCampaignStockData()

        onSetCampaignStock_thenReturn(getStockAllocationData)
        onGetOtherCampaignStock_thenReturn(otherCampaignStockData)

        viewModel.getStockAllocation(listOf(anyString()))
        viewModel.setShopId(anyString())

        joinCoroutineJob()

        verifyGetCampaignStockAllocationCalled()
        verifyGetOtherCampaignStockDataCalled()

        val expectedResult = Success(NonVariantStockAllocationResult(getStockAllocationData, otherCampaignStockData))

        verifyGetStockAllocationSuccessResult(expectedResult)
    }

    @Test
    fun `success get variant stock allocation result`() = runBlocking {
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

        viewModel.getStockAllocation(listOf(anyString()))
        viewModel.setShopId(anyString())

        joinCoroutineJob()

        verifyGetCampaignStockAllocationCalled()
        verifyGetProductVariantCalled()
        verifyGetOtherCampaignStockDataCalled()

        val expectedResult = Success(VariantStockAllocationResult(
                ProductManageVariantMapper.mapToVariantsResult(getProductVariantResponse.getProductV3),
                getStockAllocationData,
                otherCampaignStockData))

        verifyGetStockAllocationSuccessResult(expectedResult)
    }

    @Test
    fun `success update non-variant stock data`() = runBlocking {
        val editStockResponse = ProductUpdateV3Response()
        val getProductVariantResponse = createGetVariantResponse()

        onEditStock_thenReturn(editStockResponse)
        onGetProductVariant_thenReturn(getProductVariantResponse)

        viewModel.run {
            getStockAllocation(listOf(anyString()))
            setShopId(anyString())
            updateNonVariantStockCount(anyInt())
            updateNonVariantIsActive(anyBoolean())
            updateStockData()
        }

        joinCoroutineJob()

        verifyEditStockCalled()

        val expectedResult = Success(UpdateCampaignStockResult(
                anyString(),
                anyString(),
                anyInt(),
                ProductStatus.INACTIVE,
                editStockResponse.productUpdateV3Data.isSuccess,
                editStockResponse.productUpdateV3Data.header.errorMessage.firstOrNull()
        ))

        verifyProductUpdateResponseResult(expectedResult)
    }

    @Test
    fun `success update variant stock data`() = runBlocking {
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

        viewModel.run {
            getStockAllocation(listOf(anyString()))
            setShopId(anyString())
            updateVariantStockCount(anyString(), anyInt())
            updateVariantIsActive(anyString(), ProductStatus.ACTIVE)

            delay(500)
            updateStockData()
        }

        joinCoroutineJob()

        verifyEditProductVariantCalled()

        val expectedResult = Success(UpdateCampaignStockResult(
                anyString(),
                anyString(),
                anyInt(),
                ProductStatus.INACTIVE,
                editVariantResponse.productUpdateV3Data.isSuccess,
                editVariantResponse.productUpdateV3Data.header.errorMessage.firstOrNull()
        ))
        verifyProductUpdateResponseResult(expectedResult)
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

    private fun verifyGetCampaignStockAllocationCalled() {
        coVerify {
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
        assertEquals(actualResult, expectedResult)
    }

    private fun verifyProductUpdateResponseResult(expectedResult: Success<UpdateCampaignStockResult>) {
        val actualResult = viewModel.productUpdateResponseLiveData.value
        assertEquals(expectedResult, actualResult)
    }

    private suspend fun joinCoroutineJob() {
        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
    }

}