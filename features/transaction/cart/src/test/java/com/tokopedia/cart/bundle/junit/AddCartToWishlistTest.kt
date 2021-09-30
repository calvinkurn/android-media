package com.tokopedia.cart.bundle.junit

import com.tokopedia.cart.bundle.domain.model.cartlist.AddCartToWishlistData
import com.tokopedia.network.exception.ResponseErrorException
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Observable

class AddCartToWishlistTest : BaseCartTest() {

    @Test
    fun `WHEN add cart item to wishlist success THEN should render success`() {
        // Given
        val productId = "1"
        val cartId = "2"
        val isLastItem = false
        val source = "source"
        val forceExpandCollapsedUnavailableItems = false

        val addToCartWishlistData = AddCartToWishlistData().apply {
            isSuccess = true
            message = "success"
        }

        every { addCartToWishlistUseCase.createObservable(any()) } returns Observable.just(addToCartWishlistData)
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        cartListPresenter?.processAddCartToWishlist(productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)

        // Then
        verify {
            view.onAddCartToWishlistSuccess(addToCartWishlistData.message, productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)
        }
    }

    @Test
    fun `WHEN add cart item to wishlist failed THEN should render error`() {
        // Given
        val productId = "1"
        val cartId = "2"
        val isLastItem = false
        val source = "source"
        val forceExpandCollapsedUnavailableItems = false

        val addToCartWishlistData = AddCartToWishlistData().apply {
            isSuccess = false
            message = "failed"
        }

        every { addCartToWishlistUseCase.createObservable(any()) } returns Observable.just(addToCartWishlistData)
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        cartListPresenter?.processAddCartToWishlist(productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)

        // Then
        verify {
            view.showToastMessageRed(addToCartWishlistData.message)
        }
    }

    @Test
    fun `WHEN add cart item to wishlist failed with exception THEN should render error`() {
        // Given
        val productId = "1"
        val cartId = "2"
        val isLastItem = false
        val source = "source"
        val forceExpandCollapsedUnavailableItems = false
        val exception = ResponseErrorException("Error")

        val addToCartWishlistData = AddCartToWishlistData().apply {
            isSuccess = false
            message = "failed"
        }

        every { addCartToWishlistUseCase.createObservable(any()) } returns Observable.error(exception)
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        cartListPresenter?.processAddCartToWishlist(productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)

        // Then
        verify {
            view.showToastMessageRed(exception)
        }
    }
}