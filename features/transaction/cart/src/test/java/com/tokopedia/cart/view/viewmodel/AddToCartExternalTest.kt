package com.tokopedia.cart.view.viewmodel

import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalModel
import com.tokopedia.network.exception.MessageErrorException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

class AddToCartExternalTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN add to cart success THEN should render success`() {
        // Given
        val addToCartExternalModel = AddToCartExternalModel().apply {
            success = 1
            message = arrayListOf<String>().apply {
                add("Success message")
            }
        }

        coEvery { addToCartExternalUseCase(any()) } returns addToCartExternalModel
        every { userSessionInterface.userId } returns "123"

        // When
        cartViewModel.processAddToCartExternal(1)

        // Then
        verifyOrder {
            view.hideProgressLoading()
            view.showToastMessageGreen(addToCartExternalModel.message[0])
            view.refreshCartWithSwipeToRefresh()
        }
    }

    @Test
    fun `WHEN add to cart failed THEN should render success`() {
        // Given
        val errorMessage = "Error message"
        val exception = MessageErrorException(errorMessage)

        coEvery { addToCartExternalUseCase(any()) } throws exception
        every { userSessionInterface.userId } returns "123"

        // When
        cartViewModel.processAddToCartExternal(1)

        // Then
        verifyOrder {
            view.hideProgressLoading()
            view.showToastMessageRed(exception)
            view.refreshCartWithSwipeToRefresh()
        }
    }

    @Test
    fun `WHEN add to cart with view is detached THEN should not render view`() {
        // Given
        val addToCartExternalModel = AddToCartExternalModel().apply {
            success = 1
            message = arrayListOf<String>().apply {
                add("Success message")
            }
        }

        coEvery { addToCartExternalUseCase(any()) } returns addToCartExternalModel
        every { userSessionInterface.userId } returns "123"

        // When
        cartViewModel.processAddToCartExternal(1)

        // Then
        verify(inverse = true) {
            view.showProgressLoading()
        }
    }
}
