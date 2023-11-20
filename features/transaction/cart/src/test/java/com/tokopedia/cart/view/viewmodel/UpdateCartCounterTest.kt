package com.tokopedia.cart.view.viewmodel

import io.mockk.every
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class UpdateCartCounterTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN updateCartCounter then updateCartCounterUseCase should called`() {
        // GIVEN
        val counter = 3
        val result = Observable.just(counter)
        val subscriber = TestSubscriber<Int>()
        every { updateCartCounterUseCase.createObservable(any()) } returns result
        result.subscribe(subscriber)

        // WHEN
        cartViewModel.processUpdateCartCounter()

        // THEN
        subscriber.assertValue(3)
    }
}
