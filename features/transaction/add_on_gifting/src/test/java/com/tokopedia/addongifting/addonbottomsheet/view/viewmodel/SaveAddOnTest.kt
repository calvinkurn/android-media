package com.tokopedia.addongifting.addonbottomsheet.view.viewmodel

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.addongifting.addonbottomsheet.view.DataProvider
import com.tokopedia.addongifting.addonbottomsheet.view.UiEvent
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnStateResponse
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveAddOnTest : BaseAddOnTest() {

    @Test
    fun `WHEN save add on success THEN ui state should be STATE_SUCCESS_SAVE_ADD_ON`() = runTest {
        // Given
        val addOnProductData = AddOnProductData()
        val response = DataProvider.provideSaveAddOnDataSuccess()
        coEvery { saveAddOnStateUseCase.setParams(any(), false) } just Runs
        coEvery { saveAddOnStateUseCase.execute(any(), any()) } answers {
            firstArg<(SaveAddOnStateResponse) -> Unit>().invoke(response)
        }

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.saveAddOnState(addOnProductData)

        // Then
        assert(result.first().state == UiEvent.STATE_SUCCESS_SAVE_ADD_ON)
        job.cancel()
    }

    @Test
    fun `WHEN save add on got error THEN ui state should be STATE_FAILED_SAVE_ADD_ON`() = runTest {
        // Given
        val addOnProductData = AddOnProductData()
        val response = DataProvider.provideSaveAddOnDataError()
        coEvery { saveAddOnStateUseCase.setParams(any(), false) } just Runs
        coEvery { saveAddOnStateUseCase.execute(any(), any()) } answers {
            firstArg<(SaveAddOnStateResponse) -> Unit>().invoke(response)
        }

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.saveAddOnState(addOnProductData)

        // Then
        assert(result.first().state == UiEvent.STATE_FAILED_SAVE_ADD_ON)
        job.cancel()
    }

    @Test
    fun `WHEN save add on got exception THEN ui state should be STATE_FAILED_SAVE_ADD_ON`() = runTest {
        // Given
        val addOnProductData = AddOnProductData()
        coEvery { saveAddOnStateUseCase.setParams(any(), false) } just Runs
        coEvery { saveAddOnStateUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(ResponseErrorException())
        }

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEvent.toCollection(result)
        }

        // When
        viewModel.saveAddOnState(addOnProductData)

        // Then
        assert(result.first().state == UiEvent.STATE_FAILED_SAVE_ADD_ON)
        job.cancel()
    }
}
