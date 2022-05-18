package com.tokopedia.gifting.viewmodel

import com.tokopedia.gifting.domain.model.*
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product_bundle.util.getOrAwaitValue
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GiftingBottomsheetViewModelTest: GiftingBottomsheetViewModelTestFixture() {

    @Test
    fun `when getAddOn using valid id should invoke getAddOnResult and isTokocabang change`() = runBlocking {
        // Given
        coEvery {
            getAddOnUseCase.executeOnBackground()
        } returns GetAddOnResponse().apply {
            getAddOnByID.addOnByIDResponse = listOf(
                Addon().apply {
                    basic = Basic().apply { addOnLevel = "ORDER_ADDON" }
                }
            )
        }

        // When
        viewModel.getAddOn("123")
        val getAddOnResult = viewModel.getAddOnResult.getOrAwaitValue()
        val isTokocabang = viewModel.isTokoCabang.getOrAwaitValue()

        // Then
        assert(getAddOnResult.addOnByIDResponse.isNotEmpty())
        assert(isTokocabang)
    }

    @Test
    fun `when getAddOn using invalid id should invoke errorThrowable change`() = runBlocking {
        // Given
        val errorMessage = "this is error"
        coEvery {
            getAddOnUseCase.executeOnBackground()
        } returns GetAddOnResponse().apply {
            getAddOnByID.error = Error().apply {
                messages = errorMessage
            }
        }

        // When
        viewModel.getAddOn("123")
        val errorThrowable = viewModel.errorThrowable.getOrAwaitValue()

        // Then
        assert(errorThrowable.message?.contains(errorMessage).orFalse())
    }

    @Test
    fun `when getAddOn throws error should invoke errorThrowable change`() = runBlocking {
        // Given
        val errorMessage = "this is error throwable"
        coEvery {
            getAddOnUseCase.executeOnBackground()
        } throws MessageErrorException(errorMessage)

        // When
        viewModel.getAddOn("123")
        val errorThrowable = viewModel.errorThrowable.getOrAwaitValue()

        // Then
        assert(errorThrowable.message?.contains(errorMessage).orFalse())
    }
}