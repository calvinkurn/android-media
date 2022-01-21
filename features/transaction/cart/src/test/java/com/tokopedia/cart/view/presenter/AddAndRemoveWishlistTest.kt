package com.tokopedia.cart.view.presenter

import com.tokopedia.wishlist.common.listener.WishListActionListener
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.Test

class AddAndRemoveWishlistTest : BaseCartTest() {

    @Test
    fun `WHEN add to wislist THEN should call API`() {
        // GIVEN
        every { addWishListUseCase.createObservable(any(), any(), any()) } just Runs

        // WHEN
        cartListPresenter.processAddToWishlist("1", "1", object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                /* No-op */
            }

            override fun onSuccessAddWishlist(productId: String?) {
                /* No-op */
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                /* No-op */
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                /* No-op */
            }
        })

        // THEN
        verify {
            addWishListUseCase.createObservable(any(), any(), any())
        }
    }

    @Test
    fun `WHEN remove from wislist THEN should call API`() {
        // GIVEN
        every { removeWishListUseCase.createObservable(any(), any(), any()) } just Runs

        // WHEN
        cartListPresenter.processRemoveFromWishlist("1", "1", object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                /* No-op */
            }

            override fun onSuccessAddWishlist(productId: String?) {
                /* No-op */
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                /* No-op */
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                /* No-op */
            }
        })

        // THEN
        verify {
            removeWishListUseCase.createObservable(any(), any(), any())
        }
    }

}