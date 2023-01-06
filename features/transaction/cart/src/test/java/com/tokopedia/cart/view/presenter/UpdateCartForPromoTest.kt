package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.utils.DataProvider
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.network.exception.ResponseErrorException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

class UpdateCartForPromoTest : BaseCartTest() {

    @Test
    fun `WHEN update cart for promo success THEN should navigate to promo page`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData().apply {
                isCod = true
                productPrice = 1000.0
                quantity = 10
            })
        }

        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        // WHEN
        cartListPresenter.doUpdateCartForPromo()

        // THEN
        verifyOrder {
            view.hideProgressLoading()
            view.navigateToPromoRecommendation()
        }
    }

    @Test
    fun `WHEN update cart for promo failed with exception THEN should render error`() {
        // GIVEN
        val exception = ResponseErrorException("error message")

        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData().apply {
                isCod = true
                productPrice = 1000.0
                quantity = 10
            })
        }

        every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        // WHEN
        cartListPresenter.doUpdateCartForPromo()

        // THEN
        verify {
            view.showToastMessageRed(exception)
        }
    }

    @Test
    fun `WHEN update cart for promo with empty parameter THEN should not hit API`() {
        // GIVEN
        every { view.getAllSelectedCartDataList() } answers { emptyList() }

        // WHEN
        cartListPresenter.doUpdateCartForPromo()

        // THEN
        verify(inverse = true) {
            updateCartUseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN update cart for promo with view detached THEN should not render view`() {
        // GIVEN
        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.doUpdateCartForPromo()

        // THEN
        verify(inverse = true) {
            view.navigateToPromoRecommendation()
        }
    }

}
