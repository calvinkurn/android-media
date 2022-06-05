package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.domain.model.cartlist.AddCartToWishlistData
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.*
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
            status = "OK"
            success = 1
            message = "success"
        }

        every { addCartToWishlistUseCase.createObservable(any()) } returns Observable.just(addToCartWishlistData)
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        cartListPresenter.processAddCartToWishlist(productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)

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
            status = "ERROR"
            success = 0
            message = "failed"
        }

        every { addCartToWishlistUseCase.createObservable(any()) } returns Observable.just(addToCartWishlistData)
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        cartListPresenter.processAddCartToWishlist(productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)

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

        every { addCartToWishlistUseCase.createObservable(any()) } returns Observable.error(exception)
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        cartListPresenter.processAddCartToWishlist(productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)

        // Then
        verify {
            view.showToastMessageRed(exception)
        }
    }

    @Test
    fun `WHEN add cart item to wishlist with view is not attached THEN should not render view`() {
        // GIVEN
        val productId = "1"
        val cartId = "2"
        val isLastItem = false
        val source = "source"
        val forceExpandCollapsedUnavailableItems = false

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.processAddCartToWishlist(productId, cartId, isLastItem, source, forceExpandCollapsedUnavailableItems)

        // THEN
        verify(inverse = true) {
            view.onAddCartToWishlistSuccess(any(), any(), any(), any(), any(), any())
        }
    }

    @Test
    fun `verify add to wishlistv2 returns success` () {
        val productId = "123"
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishListV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishListV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)
        every { userSessionInterface.userId } returns "1"

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        cartListPresenter.processAddToWishlistV2(productId, "1", mockListener)

        verify { addToWishListV2UseCase.setParams(productId, "1") }
        coVerify { addToWishListV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify add to wishlistv2 returns fail` () {
        val productId = "123"
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishListV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishListV2UseCase.executeOnBackground() } returns Fail(mockThrowable)
        every { userSessionInterface.userId } returns "1"

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        cartListPresenter.processAddToWishlistV2(productId, "1", mockListener)

        verify { addToWishListV2UseCase.setParams(recommendationItem.productId.toString(), "1") }
        coVerify { addToWishListV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove wishlistV2 returns success`(){
        val productId = "123"
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistRemoveV2)
        every { userSessionInterface.userId } returns "1"

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        cartListPresenter.processRemoveFromWishlistV2(productId, "1", mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, "1") }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove wishlistV2 returns fail`(){
        val productId = "123"
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() }returns Fail(mockThrowable)
        every { userSessionInterface.userId } returns "1"

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        cartListPresenter.processRemoveFromWishlistV2(productId, "1", mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, "1") }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }
}