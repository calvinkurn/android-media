package com.tokopedia.cart.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.cart.view.uimodel.CartGlobalEvent
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class UpdateCartCounterTest : BaseCartViewModelTest() {

    private lateinit var cartGlobalEventObserver: Observer<CartGlobalEvent>

    override fun setUp() {
        super.setUp()
        cartGlobalEventObserver = mockk { every { onChanged(any()) } just Runs }
        cartViewModel.globalEvent.observeForever(cartGlobalEventObserver)
    }

    @Test
    fun `WHEN updateCartCounter then updateCartCounterUseCase should called`() {
        // GIVEN
        val resultExpected = 3
        coEvery { updateCartCounterUseCase(any()) } returns resultExpected

        // WHEN
        cartViewModel.processUpdateCartCounter()

        // THEN
        Assert.assertEquals(
            CartGlobalEvent.CartCounterUpdated(resultExpected),
            cartViewModel.globalEvent.value
        )
        coVerify(inverse = true) {
            updateCartCounterUseCase(any())
        }
    }
}
