package com.tokopedia.cart.view.presenter

import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import com.tokopedia.wishlist.common.response.GetWishlistResponse
import com.tokopedia.wishlist.common.response.WishlistDataResponse
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test
import rx.Observable

class GetWishlistTest : BaseCartTest() {

    @Test
    fun `WHEN get wishlist success THEN should render wishlist section`() {
        // GIVEN
        val response = GetWishlistResponse().apply {
            gqlWishList = WishlistDataResponse().apply {
                wishlistDataList = mutableListOf<Wishlist>().apply {
                    add(Wishlist())
                }
            }
        }

        every { getWishlistUseCase.createObservable(any()) } returns Observable.just(response)

        // WHEN
        cartListPresenter.processGetWishlistData()

        // THEN
        verifyOrder {
            view.renderWishlist(response.gqlWishList?.wishlistDataList, true)
            view.setHasTriedToLoadWishList()
            view.stopAllCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN get wishlist empty THEN should not render wishlist section`() {
        // GIVEN
        val response = GetWishlistResponse().apply {
            gqlWishList = WishlistDataResponse().apply {
                wishlistDataList = mutableListOf()
            }
        }

        every { getWishlistUseCase.createObservable(any()) } returns Observable.just(response)

        // WHEN
        cartListPresenter.processGetWishlistData()

        // THEN
        verify(inverse = true) {
            view.renderWishlist(response.gqlWishList?.wishlistDataList, false)
        }

        verify {
            view.setHasTriedToLoadWishList()
            view.stopAllCartPerformanceTrace()
        }
    }

    @Test
    fun `WHEN get wishlist failed THEN should render error`() {
        // GIVEN
        every { getWishlistUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())

        // WHEN
        cartListPresenter.processGetWishlistData()

        // THEN
        verifyOrder {
            view.setHasTriedToLoadWishList()
            view.stopAllCartPerformanceTrace()
        }
    }
}