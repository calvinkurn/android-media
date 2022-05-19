package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Observable

class SaveCheckboxStateTest : BaseCartTest() {

    @Test
    fun `WHEN trigger save checkbox state THEN use case should be called`() {
        // GIVEN
        val cartItemDataList = ArrayList<CartItemHolderData>().apply {
            add(
                    CartItemHolderData(
                            isSelected = true,
                            cartId = "123"
                    )
            )
        }

        every { setCartlistCheckboxStateUseCase.createObservable(any()) } returns Observable.just(true)
        every { setCartlistCheckboxStateUseCase.buildRequestParams(any()) } returns RequestParams.EMPTY

        // WHEN
        cartListPresenter.saveCheckboxState(cartItemDataList)

        // THEN
        verify {
            setCartlistCheckboxStateUseCase.createObservable(any())
        }
    }

}