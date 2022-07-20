package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalModel
import com.tokopedia.network.exception.MessageErrorException
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test
import rx.Observable

class AddToCartExternalTest : BaseCartTest() {

    @Test
    fun `WHEN add to cart success THEN should render success`() {
        // Given
        val addToCartExternalModel = AddToCartExternalModel().apply {
            success = 1
            message = arrayListOf<String>().apply {
                add("Success message")
            }
        }

        every { addToCartExternalUseCase.createObservable(any()) } returns Observable.just(addToCartExternalModel)
        every { userSessionInterface.userId } returns "123"

        // When
        cartListPresenter.processAddToCartExternal(1)

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

        every { addToCartExternalUseCase.createObservable(any()) } returns Observable.error(exception)
        every { userSessionInterface.userId } returns "123"

        // When
        cartListPresenter.processAddToCartExternal(1)

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

        every { addToCartExternalUseCase.createObservable(any()) } returns Observable.just(addToCartExternalModel)
        every { userSessionInterface.userId } returns "123"

        cartListPresenter.detachView()

        // When
        cartListPresenter.processAddToCartExternal(1)

        // Then
        verify(inverse = true) {
            view.showProgressLoading()
        }
    }

}