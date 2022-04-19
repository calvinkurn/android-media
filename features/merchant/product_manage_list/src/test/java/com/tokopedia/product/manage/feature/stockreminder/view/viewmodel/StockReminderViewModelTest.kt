package com.tokopedia.product.manage.feature.stockreminder.view.viewmodel

import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateUpdateDataWrapper
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateUpdateProduct
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.UpdateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetDataWrapper
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetProduct
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.ProductWareHouse
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
class StockReminderViewModelTest: StockReminderViewModelTestFixture() {

    @Test
    fun `when get stock reminder success should return result`() {
        runBlocking {
            val productId = "123"
            val wareHouseId = "321"
            val stock = 10
            val price = 10000L
            val threshold = 2
            val shopId = "1011"

            val productWareHouse = ProductWareHouse(productId,wareHouseId,stock,price,threshold,shopId)
            val product = GetProduct(productId, listOf(productWareHouse))
            val data = listOf(product)
            val dataWrapper = GetDataWrapper(data)
            val getStockReminderResponse = GetStockReminderResponse(dataWrapper)

            onGetStockReminder_thenReturn(getStockReminderResponse)

            viewModel.getStockReminder(productId)

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

            val product = CreateUpdateProduct(productId,wareHouseId,threshold)
            val data = listOf(product)
            val dataWrapper = CreateUpdateDataWrapper(data)
            val createStockReminderResponse = CreateStockReminderResponse(dataWrapper)

            onCreateStockReminder_thenReturn(createStockReminderResponse)

            viewModel.createStockReminder(shopId, productId, wareHouseId, threshold)

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

            viewModel.createStockReminder(anyString(), anyString(), anyString(), anyString())

            verifyCreateStockReminderUseCaseCalled()
            verifyCreateStockReminderFail()
        }
    }

    @Test
    fun `when update stock reminder success should return result`() {
        runBlocking {
            val productId = "123"
            val wareHouseId = "321"
            val threshold = "2"
            val shopId = "1011"

            val product = CreateUpdateProduct(productId,wareHouseId,threshold)
            val data = listOf(product)
            val dataWrapper = CreateUpdateDataWrapper(data)
            val updateStockReminderResponse = UpdateStockReminderResponse(dataWrapper)

            onUpdateStockReminder_thenReturn(updateStockReminderResponse)

            viewModel.updateStockReminder(shopId, productId, wareHouseId, threshold)

            val expectedResult = Success(UpdateStockReminderResponse(CreateUpdateDataWrapper(data)))

            verifyUpdateStockReminderUseCaseCalled()
            verifyUpdateStockReminderResult(expectedResult)
        }
    }

    @Test
    fun `when update stock reminder error should set live data fail`() {
        runBlocking {
            val error = NullPointerException()
            onUpdateStockReminder_thenThrow(error)

            viewModel.updateStockReminder(anyString(), anyString(), anyString(), anyString())

            verifyUpdateStockReminderUseCaseCalled()
            verifyUpdateStockReminderFail()
        }
    }

    private suspend fun onGetStockReminder_thenReturn(getStockReminderResponse: GetStockReminderResponse) {
        coEvery { stockReminderDataUseCase.executeGetStockReminder() } returns getStockReminderResponse
    }

    private suspend fun onCreateStockReminder_thenReturn(createStockReminderResponse: CreateStockReminderResponse) {
        coEvery { stockReminderDataUseCase.executeCreateStockReminder() } returns createStockReminderResponse
    }

    private suspend fun onUpdateStockReminder_thenReturn(updateStockReminderResponse: UpdateStockReminderResponse) {
        coEvery { stockReminderDataUseCase.executeUpdateStockReminder() } returns updateStockReminderResponse
    }

    private suspend fun onGetStockReminder_thenThrow(ex: Exception) {
        coEvery { stockReminderDataUseCase.executeGetStockReminder() } throws ex
    }

    private suspend fun onCreateStockReminder_thenThrow(ex: Exception) {
        coEvery { stockReminderDataUseCase.executeCreateStockReminder() } throws ex
    }

    private suspend fun onUpdateStockReminder_thenThrow(ex: Exception) {
        coEvery { stockReminderDataUseCase.executeUpdateStockReminder() } throws ex
    }

    private fun verifyGetStockReminderUseCaseCalled() {
        coVerify { stockReminderDataUseCase.executeGetStockReminder() }
    }

    private fun verifyCreateStockReminderUseCaseCalled() {
        coVerify { stockReminderDataUseCase.executeCreateStockReminder() }
    }

    private fun verifyUpdateStockReminderUseCaseCalled() {
        coVerify { stockReminderDataUseCase.executeUpdateStockReminder() }
    }

    private fun verifyGetStockReminderResult(expectedResult: Success<GetStockReminderResponse>) {
        val actualResult = viewModel.getStockReminderLiveData.value as Success<GetStockReminderResponse>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifyCreateStockReminderResult(expectedResult: Success<CreateStockReminderResponse>) {
        val actualResult = viewModel.createStockReminderLiveData.value as Success<CreateStockReminderResponse>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifyUpdateStockReminderResult(expectedResult: Success<UpdateStockReminderResponse>) {
        val actualResult = viewModel.updateStockReminderLiveData.value as Success<UpdateStockReminderResponse>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifyGetStockReminderFail() {
        assert(viewModel.getStockReminderLiveData.value is Fail)
    }

    private fun verifyCreateStockReminderFail() {
        assert(viewModel.createStockReminderLiveData.value is Fail)
    }

    private fun verifyUpdateStockReminderFail() {
        assert(viewModel.updateStockReminderLiveData.value is Fail)
    }
}