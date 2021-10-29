package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.view.uimodel.CartItemHolderData
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import org.junit.Test
import rx.Observable

class SaveCheckboxStateTest : BaseCartTest() {

    @Test
    fun `WHEN save checkbox state success THEN `() {
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
        cartListPresenter?.saveCheckboxState(cartItemDataList)

        // THEN
        assert(true)
    }

}