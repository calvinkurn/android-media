package com.tokopedia.addongifting.addonbottomsheet.view.viewmodel

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductResponse
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate.GetAddOnSavedStateResponse
import com.tokopedia.addongifting.addonbottomsheet.view.DataProvider
import com.tokopedia.addongifting.addonbottomsheet.view.UiEvent
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class LoadAddOnTest: BaseAddOnTest() {

    @Test
    fun `WHEN load add on data and ad on saved state success THEN ui state should be STATE_SUCCESS_LOAD_ADD_ON_DATA`() = runBlockingTest {
        // GIVEN
        val addOnProductData = AddOnProductData()
        val addOnSuccessResponse = DataProvider.provideLoadAddOnDataSuccess()
        coEvery { getAddOnByProductUseCase.setParams(any()) } just Runs
        coEvery { getAddOnByProductUseCase.execute(any(), any()) } answers {
            firstArg<(GetAddOnByProductResponse) -> Unit>().invoke(addOnSuccessResponse)
        }

        val addOnSavedStateSuccessResponse = DataProvider.provideLoadAddOnSavedStateDataSuccess()
        coEvery { getAddOnSavedStateUseCase.setParams(any()) } just Runs
        coEvery { getAddOnSavedStateUseCase.execute(any(), any()) } answers {
            firstArg<(GetAddOnSavedStateResponse) -> Unit>().invoke(addOnSavedStateSuccessResponse)
        }

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // WHEN
        viewModel.loadAddOnData(addOnProductData)

        // THEN
        assert(result.first().state == UiEvent.STATE_SUCCESS_LOAD_ADD_ON_DATA)
        job.cancel()
    }

    @Test
    fun `WHEN load add on data error THEN ui state should be STATE_FAILED_LOAD_ADD_ON_DATA`() = runBlockingTest {
        // GIVEN
        val addOnProductData = AddOnProductData()
        coEvery { getAddOnByProductUseCase.setParams(any()) } just Runs
        coEvery { getAddOnByProductUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(ResponseErrorException())
        }

        // store the result
        val result = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.toCollection(result)
        }

        // WHEN
        viewModel.loadAddOnData(addOnProductData)

        // THEN
        assert(result.first().state == UiEvent.STATE_FAILED_LOAD_ADD_ON_DATA)
        job.cancel()
    }

}