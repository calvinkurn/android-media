package com.tokopedia.cart.view.viewmodel

import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test

class SaveCheckboxStateTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN trigger save checkbox state THEN use case should be called`() {
        coEvery { setCartlistCheckboxStateUseCase(any()) } returns true

        // WHEN
        cartViewModel.saveCheckboxState()

        // THEN
        coVerify {
            setCartlistCheckboxStateUseCase(any())
        }
    }
}
