package com.tokopedia.product.manage.feature.stockreminder.view.viewmodel

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.data.model.response.Header
import com.tokopedia.product.manage.common.feature.variant.data.model.GetProductV3
import com.tokopedia.product.manage.common.feature.variant.data.model.Variant
import com.tokopedia.product.manage.common.feature.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateUpdateDataWrapper
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateUpdateProduct
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetDataWrapper
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetProduct
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.ProductWareHouse
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel
import com.tokopedia.product.manage.feature.stockreminder.view.data.mapper.ProductStockReminderMapper
import com.tokopedia.shop.common.data.source.cloud.model.MaxStockThresholdResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class StockReminderViewModelTest : StockReminderViewModelTestFixture() {

    @Test
    fun `when get stock reminder success should return result`() {
        runBlocking {
            val productId = "123"
            val wareHouseId = "321"
            val stock = 10
            val price = 10000L
            val threshold = 2
            val shopId = "1011"

            val productWareHouse =
                ProductWareHouse(productId, wareHouseId, stock, price.toString(), threshold, shopId)
            val product = GetProduct(productId, listOf(productWareHouse))
            val data = listOf(product)
            val dataWrapper = GetDataWrapper(data)
            val getStockReminderResponse = GetStockReminderResponse(dataWrapper)

            onGetStockReminder_thenReturn(getStockReminderResponse)

            val showLoading = true
            val hideLoading = false

            viewModel.showLoading()
            verifyLoadingState(showLoading)
            viewModel.getStockReminder(productId)

            viewModel.hideLoading()
            verifyLoadingState(hideLoading)

            val expectedResult = Success(GetStockReminderResponse(GetDataWrapper(data)))

            verifyGetStockReminderUseCaseCalled()
            verifyGetStockReminderResult(expectedResult)
        }
    }

    @Test
    fun `when get stock reminder error should set live data fail`() {
        runBlocking {
            val error = NullPointerException()
            onGetStockReminder_thenThrow(error)

            viewModel.getStockReminder(anyString())

            verifyGetStockReminderUseCaseCalled()
            verifyGetStockReminderFail()
        }
    }

    @Test
    fun `when create stock reminder success should return result`() {
        runBlocking {
            val productId = "123"
            val wareHouseId = "321"
            val threshold = "2"
            val shopId = "1011"

            val product = CreateUpdateProduct(productId, wareHouseId, threshold)
            val data = listOf(product)
            val dataWrapper = CreateUpdateDataWrapper(data)
            val createStockReminderResponse = CreateStockReminderResponse(dataWrapper)

            onCreateStockReminder_thenReturn(createStockReminderResponse)

            viewModel.createStockReminder(shopId, arrayListOf())

            val expectedResult = Success(CreateStockReminderResponse(CreateUpdateDataWrapper(data)))

            verifyCreateStockReminderUseCaseCalled()
            verifyCreateStockReminderResult(expectedResult)
        }
    }

    @Test
    fun `when create stock reminder error should set live data fail`() {
        runBlocking {
            val error = NullPointerException()
            onCreateStockReminder_thenThrow(error)

            viewModel.createStockReminder(anyString(), arrayListOf())

            verifyCreateStockReminderUseCaseCalled()
            verifyCreateStockReminderFail()
        }
    }

    @Test
    fun `when get product success should return result`() {
        runBlocking {
            val productId = "123"
            val wareHouseId = "321"
            val shopId = "1234"

            val variant = Variant(listOf(), listOf(), listOf())
            val getProductV3 =
                GetProductV3(productId, String.EMPTY, String.EMPTY, Int.ZERO, Int.ZERO, Int.ZERO, variant)
            val getProductVariantResponse = GetProductVariantResponse(getProductV3)

            onGetProduct_thenReturn(productId, wareHouseId, getProductVariantResponse)

            viewModel.getProduct(productId, wareHouseId, shopId)

            val expectedResult = Success(ProductStockReminderMapper.mapToProductResult(getProductV3, null))

            verifyGetProductUseCaseCalled(productId, wareHouseId)
            verifyGetProduct(expectedResult)
        }
    }

    @Test
    fun `when get product and max stock success should set max stock live data`() {
        runBlocking {
            val productId = "123"
            val wareHouseId = "321"
            val shopId = "1234"
            val maxStock = 5

            val variant = Variant(listOf(), listOf(), listOf())
            val getProductV3 =
                GetProductV3(productId, String.EMPTY, String.EMPTY, Int.ZERO, Int.ZERO, Int.ZERO, variant)
            val getProductVariantResponse = GetProductVariantResponse(getProductV3)

            onGetProduct_thenReturn(productId, wareHouseId, getProductVariantResponse)
            onGetMaxStockThreshold_thenReturn(maxStock)

            viewModel.getProduct(productId, wareHouseId, shopId)

            verifyGetProductUseCaseCalled(productId, wareHouseId)
            verifyMaxStockEquals(maxStock)
        }
    }

    @Test
    fun `when get product error should set live data fail`() {
        runBlocking {
            val productId = "123"
            val wareHouseId = "321"
            val shopId = "1234"
            val error = NullPointerException()
            onGetProduct_thenThrow(productId, wareHouseId, error)

            viewModel.getProduct(productId, wareHouseId, shopId)

            verifyGetProductUseCaseCalled(productId, wareHouseId)
            verifyGetProductFail()
        }
    }

    private suspend fun onGetStockReminder_thenReturn(getStockReminderResponse: GetStockReminderResponse) {
        coEvery { stockReminderDataUseCase.executeGetStockReminder() } returns getStockReminderResponse
    }

    private suspend fun onCreateStockReminder_thenReturn(createStockReminderResponse: CreateStockReminderResponse) {
        coEvery { stockReminderDataUseCase.executeCreateStockReminder() } returns createStockReminderResponse
    }

    private suspend fun onGetStockReminder_thenThrow(ex: Exception) {
        coEvery { stockReminderDataUseCase.executeGetStockReminder() } throws ex
    }

    private suspend fun onCreateStockReminder_thenThrow(ex: Exception) {
        coEvery { stockReminderDataUseCase.executeCreateStockReminder() } throws ex
    }

    private suspend fun onGetMaxStockThreshold_thenReturn(maxStock: Int?) {
        coEvery {
            getMaxStockThresholdUseCase.execute(any())
        } returns MaxStockThresholdResponse(
            getIMSMeta = MaxStockThresholdResponse.GetIMSMeta(
                data = MaxStockThresholdResponse.GetIMSMeta.Data(
                    maxStockThreshold = maxStock?.toString().orEmpty()
                ),
                header = Header()
            )
        )
    }

    private suspend fun onGetProduct_thenThrow(
        productId: String,
        warehouseId: String, ex: Exception
    ) {
        coEvery {
            stockReminderDataUseCase.executeGetProductStockReminder(
                productId,
                warehouseId
            )
        } throws ex
    }

    private suspend fun onGetProduct_thenReturn(
        productId: String,
        warehouseId: String,
        getProductVariantResponse: GetProductVariantResponse
    ) {
        coEvery {
            stockReminderDataUseCase.executeGetProductStockReminder(
                productId,
                warehouseId
            )
        } returns getProductVariantResponse
    }

    private fun verifyLoadingState(expectedResult: Boolean) {
        val actualResult =
            viewModel.showLoading.value
        assertEquals(expectedResult, actualResult)
    }

    private fun verifyGetStockReminderUseCaseCalled() {
        coVerify { stockReminderDataUseCase.executeGetStockReminder() }
    }

    private fun verifyCreateStockReminderUseCaseCalled() {
        coVerify { stockReminderDataUseCase.executeCreateStockReminder() }
    }

    private fun verifyGetStockReminderResult(expectedResult: Success<GetStockReminderResponse>) {
        val actualResult =
            viewModel.getStockReminderLiveData.value as Success<GetStockReminderResponse>
        assertEquals(expectedResult, actualResult)
    }


    private fun verifyCreateStockReminderResult(expectedResult: Success<CreateStockReminderResponse>) {
        val actualResult =
            viewModel.createStockReminderLiveData.value as Success<CreateStockReminderResponse>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifyGetStockReminderFail() {
        assert(viewModel.getStockReminderLiveData.value is Fail)
    }

    private fun verifyCreateStockReminderFail() {
        assert(viewModel.createStockReminderLiveData.value is Fail)
    }

    private fun verifyGetProductUseCaseCalled(productId: String, warehouseId: String) {
        coVerify { stockReminderDataUseCase.executeGetProductStockReminder(productId, warehouseId) }
    }

    private fun verifyGetProduct(expectedResult: Success<List<ProductStockReminderUiModel>>) {
        val actualResult =
            viewModel.getProductLiveData.value as Success<List<ProductStockReminderUiModel>>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifyMaxStockEquals(expectedResult: Int) {
        val actualResult = viewModel.maxStockLiveData.value
        assertEquals(expectedResult, actualResult)
    }

    private fun verifyGetProductFail() {
        assert(viewModel.getProductLiveData.value is Fail)
    }
}
