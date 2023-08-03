package com.tokopedia.addon.viewmodel

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product_bundle.util.getOrAwaitValue
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnStateResponse
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnsResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AddOnViewModelTest: AddOnViewModelTestFixture() {

    /*@Test
    fun `when getAddOn using valid id should invoke getAddOnResult`() = runBlocking {
        // Given
        coEvery {
            getAddOnUseCase.executeOnBackground()
        } returns GetAddOnByProductResponse(
            GetAddOnByProduct(
                addOnByProductResponse = listOf(
                    AddOnByProductResponse(
                        addons = listOf(
                            Addon(),
                            Addon()
                        )
                    )
                )
            )
        )

        // When
        viewModel.getAddOn("", "", false, false)
        val getAddOnResult = viewModel.getAddOnResult.getOrAwaitValue()

        // Then
        assert(getAddOnResult.isNotEmpty())
    }

    @Test
    fun `when getAddOn throws error should invoke errorThrowable`() = runBlocking {
        // Given
        val errorMessage = "this is error throwable"
        coEvery {
            getAddOnUseCase.executeOnBackground()
        } throws MessageErrorException(errorMessage)

        // When
        viewModel.getAddOn("", "", false, false)
        val errorThrowable = viewModel.errorThrowable.getOrAwaitValue()

        // Then
        assert(errorThrowable.message?.contains(errorMessage).orFalse())
    }*/

    /*@Test
    fun `when getAddOnAggregatedData using valid id should invoke aggregatedData`() = runBlocking {
        // Given
        coEvery {
            getAddOnDetailUseCase.executeOnBackground()
        } returns GetAddOnResponse()

        // When
        viewModel.getAddOnAggregatedData(listOf("123"), addonTypes, addOnWidgetParam)
        val result = viewModel.aggregatedData.getOrAwaitValue()

        // Then
        assert(result.isGetDataSuccess)
    }*/

    /*@Test
    fun `when getAddOnAggregatedData throws error should invoke errorThrowable`() = runBlocking {
        // Given
        val errorMessage = "this is error throwable"
        coEvery {
            getAddOnDetailUseCase.executeOnBackground()
        } throws MessageErrorException(errorMessage)

        // When
        viewModel.getAddOnAggregatedData(listOf("123"), addonTypes, addOnWidgetParam)
        val result = viewModel.aggregatedData.getOrAwaitValue()

        // Then
        assert(!result.isGetDataSuccess)
    }*/

    @Test
    fun `when saveAddOnState success, should invoke saveSelectionResult as Success`() = runBlocking {
        // Given
        mockSaveAddOnStateUseCaseResponse(SaveAddOnStateResponse(
            saveAddOns = SaveAddOnsResponse()
        ))

        // When
        viewModel.saveAddOnState(123123, "normal")
        val result = viewModel.saveSelectionResult.getOrAwaitValue()

        // Then
        assert((result as Success).data.isNotEmpty())
    }

    @Test
    fun `when saveAddOnState has server error, should invoke saveSelectionResult as Fail`() = runBlocking {
        // Given
        mockSaveAddOnStateUseCaseResponse(SaveAddOnStateResponse(
            saveAddOns = SaveAddOnsResponse(
                errorMessage = listOf("server issue")
            )
        ))

        // When
        viewModel.saveAddOnState(123123, "normal")
        val result = viewModel.saveSelectionResult.getOrAwaitValue()

        // Then
        assert((result as Fail).throwable.message == "server issue")
    }

    @Test
    fun `when saveAddOnState has local error, should invoke saveSelectionResult as Fail`() = runBlocking {
        // Given
        mockSaveAddOnStateUseCaseResponse(ResponseErrorException(), isMockError = true)

        // When
        viewModel.saveAddOnState(123123, "normal")
        val result = viewModel.saveSelectionResult.getOrAwaitValue()

        // Then
        assert(result is Fail)
    }

    private fun <R> mockSaveAddOnStateUseCaseResponse(invokedValue: R, isMockError: Boolean = false) {
        coEvery { saveAddOnStateUseCase.setParams(any(), any()) } just Runs
        coEvery {
            saveAddOnStateUseCase.execute(any(), any())
        } answers {
            if (isMockError) {
                secondArg<(R) -> Unit>().invoke(invokedValue)
            } else {
                firstArg<(R) -> Unit>().invoke(invokedValue)
            }
        }
        viewModel.setSelectedAddons(
            listOf(AddOnGroupUIModel())
        )
        viewModel.modifiedAddOnGroups.getOrAwaitValue()
    }
}
