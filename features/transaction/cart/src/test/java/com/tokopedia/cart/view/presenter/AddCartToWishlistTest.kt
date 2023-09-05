package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.domain.model.cartlist.AddCartToWishlistData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.WishlistV2Params
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.GetWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
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

        coEvery { addCartToWishlistUseCase(any()) } returns addToCartWishlistData
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        cartListPresenter.processAddCartToWishlist(
            productId,
            cartId,
            isLastItem,
            source,
            forceExpandCollapsedUnavailableItems
        )

        // Then
        verify {
            view.onAddCartToWishlistSuccess(
                addToCartWishlistData.message,
                productId,
                cartId,
                isLastItem,
                source,
                forceExpandCollapsedUnavailableItems
            )
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

        coEvery { addCartToWishlistUseCase(any()) } returns addToCartWishlistData
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        cartListPresenter.processAddCartToWishlist(
            productId,
            cartId,
            isLastItem,
            source,
            forceExpandCollapsedUnavailableItems
        )

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

        coEvery { addCartToWishlistUseCase(any()) } throws exception
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)

        // When
        cartListPresenter.processAddCartToWishlist(
            productId,
            cartId,
            isLastItem,
            source,
            forceExpandCollapsedUnavailableItems
        )

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
        cartListPresenter.processAddCartToWishlist(
            productId,
            cartId,
            isLastItem,
            source,
            forceExpandCollapsedUnavailableItems
        )

        // THEN
        verify(inverse = true) {
            view.onAddCartToWishlistSuccess(any(), any(), any(), any(), any(), any())
        }
    }

    @Test
    fun `verify add to wishlistv2 returns success`() {
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
    fun `verify add to wishlistv2 returns fail`() {
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
    fun `verify remove wishlistV2 returns success`() {
        val productId = "123"
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(
            resultWishlistRemoveV2
        )
        every { userSessionInterface.userId } returns "1"

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        cartListPresenter.processRemoveFromWishlistV2(productId, "1", mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, "1") }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove wishlistV2 returns fail`() {
        val productId = "123"
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)
        every { userSessionInterface.userId } returns "1"

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        cartListPresenter.processRemoveFromWishlistV2(productId, "1", mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, "1") }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify get wishlistV2 returns success`() {
        val districtId = "123"
        val cityId = "45"
        val lat = "10.2131"
        val long = "12.01324"
        val postalCode = "12345"
        val addressId = "123"
        val lca = LocalCacheModel(
            district_id = districtId,
            city_id = cityId,
            lat = lat,
            long = long,
            postal_code = postalCode,
            address_id = addressId
        )
        val wishlistParam = WishlistV2Params(
            source = "cart",
            wishlistChosenAddress = WishlistV2Params.WishlistChosenAddress(
                districtId = districtId,
                cityId = cityId,
                latitude = lat,
                longitude = long,
                postalCode = postalCode,
                addressId = addressId
            )
        )
        val wishlistV2 = GetWishlistV2Response.Data.WishlistV2(totalData = 10)

        every { getWishlistV2UseCase.setParams(wishlistParam) } just Runs
        coEvery { getWishlistV2UseCase.executeOnBackground() } returns Success(wishlistV2)

        cartListPresenter.setLocalizingAddressData(lca)
        cartListPresenter.processGetWishlistV2Data()

        verify { getWishlistV2UseCase.setParams(wishlistParam) }
        coVerify { getWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify get wishlistV2 returns fail`() {
        val mockThrowable = mockk<Throwable>("fail")
        val districtId = "123"
        val cityId = "45"
        val lat = "10.2131"
        val long = "12.01324"
        val postalCode = "12345"
        val addressId = "123"
        val lca = LocalCacheModel(
            district_id = districtId,
            city_id = cityId,
            lat = lat,
            long = long,
            postal_code = postalCode,
            address_id = addressId
        )
        val wishlistParam = WishlistV2Params(
            source = "cart",
            wishlistChosenAddress = WishlistV2Params.WishlistChosenAddress(
                districtId = districtId,
                cityId = cityId,
                latitude = lat,
                longitude = long,
                postalCode = postalCode,
                addressId = addressId
            )
        )

        every { getWishlistV2UseCase.setParams(wishlistParam) } just Runs
        coEvery { getWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        cartListPresenter.setLocalizingAddressData(lca)
        cartListPresenter.processGetWishlistV2Data()

        verify { getWishlistV2UseCase.setParams(wishlistParam) }
        coVerify { getWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify get wishlistV2 returns not empty items`() {
        val listItem = arrayListOf<GetWishlistV2Response.Data.WishlistV2.Item>()
        listItem.add(GetWishlistV2Response.Data.WishlistV2.Item(id = "1"))
        listItem.add(GetWishlistV2Response.Data.WishlistV2.Item(id = "2"))
        listItem.add(GetWishlistV2Response.Data.WishlistV2.Item(id = "3"))
        listItem.add(GetWishlistV2Response.Data.WishlistV2.Item(id = "4"))
        listItem.add(GetWishlistV2Response.Data.WishlistV2.Item(id = "5"))
        val wishlistV2 = GetWishlistV2Response.Data.WishlistV2(totalData = 5, items = listItem)
        val districtId = "123"
        val cityId = "45"
        val lat = "10.2131"
        val long = "12.01324"
        val postalCode = "12345"
        val addressId = "123"
        val lca = LocalCacheModel(
            district_id = districtId,
            city_id = cityId,
            lat = lat,
            long = long,
            postal_code = postalCode,
            address_id = addressId
        )
        val wishlistParam = WishlistV2Params(
            source = "cart",
            wishlistChosenAddress = WishlistV2Params.WishlistChosenAddress(
                districtId = districtId,
                cityId = cityId,
                latitude = lat,
                longitude = long,
                postalCode = postalCode,
                addressId = addressId
            )
        )

        every { getWishlistV2UseCase.setParams(wishlistParam) } just Runs
        coEvery { getWishlistV2UseCase.executeOnBackground() } returns Success(wishlistV2)

        cartListPresenter.setLocalizingAddressData(lca)
        cartListPresenter.processGetWishlistV2Data()

        verify { getWishlistV2UseCase.setParams(wishlistParam) }
        coVerify { getWishlistV2UseCase.executeOnBackground() }
        verifyOrder {
            view.renderWishlistV2(listItem, true)
            view.setHasTriedToLoadWishList()
            view.stopAllCartPerformanceTrace()
        }
    }

    @Test
    fun `verify get wishlistV2 returns fail and verify view`() {
        val mockThrowable = mockk<Throwable>("fail")
        val districtId = "123"
        val cityId = "45"
        val lat = "10.2131"
        val long = "12.01324"
        val postalCode = "12345"
        val addressId = "123"
        val lca = LocalCacheModel(
            district_id = districtId,
            city_id = cityId,
            lat = lat,
            long = long,
            postal_code = postalCode,
            address_id = addressId
        )
        val wishlistParam = WishlistV2Params(
            source = "cart",
            wishlistChosenAddress = WishlistV2Params.WishlistChosenAddress(
                districtId = districtId,
                cityId = cityId,
                latitude = lat,
                longitude = long,
                postalCode = postalCode,
                addressId = addressId
            )
        )

        every { getWishlistV2UseCase.setParams(wishlistParam) } just Runs
        coEvery { getWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        cartListPresenter.setLocalizingAddressData(lca)
        cartListPresenter.processGetWishlistV2Data()

        verify { getWishlistV2UseCase.setParams(wishlistParam) }
        coVerify { getWishlistV2UseCase.executeOnBackground() }
        verifyOrder {
            view.setHasTriedToLoadWishList()
            view.stopAllCartPerformanceTrace()
        }
    }

    @Test
    fun `verify get wishlistV2 returns fail when view is null`() {
        val mockThrowable = mockk<Throwable>("fail")
        val districtId = "123"
        val cityId = "45"
        val lat = "10.2131"
        val long = "12.01324"
        val postalCode = "12345"
        val addressId = "123"
        val lca = LocalCacheModel(
            district_id = districtId,
            city_id = cityId,
            lat = lat,
            long = long,
            postal_code = postalCode,
            address_id = addressId
        )
        val wishlistParam = WishlistV2Params(
            source = "cart",
            wishlistChosenAddress = WishlistV2Params.WishlistChosenAddress(
                districtId = districtId,
                cityId = cityId,
                latitude = lat,
                longitude = long,
                postalCode = postalCode,
                addressId = addressId
            )
        )

        every { getWishlistV2UseCase.setParams(wishlistParam) } just Runs
        coEvery { getWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        cartListPresenter.setLocalizingAddressData(lca)
        cartListPresenter.processGetWishlistV2Data()
        cartListPresenter.detachView()

        verify { getWishlistV2UseCase.setParams(wishlistParam) }
        coVerify { getWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify get wishlistV2 returns success when view is null`() {
        val wishlistV2 = GetWishlistV2Response.Data.WishlistV2(totalData = 10)
        val districtId = "123"
        val cityId = "45"
        val lat = "10.2131"
        val long = "12.01324"
        val postalCode = "12345"
        val addressId = "123"
        val lca = LocalCacheModel(
            district_id = districtId,
            city_id = cityId,
            lat = lat,
            long = long,
            postal_code = postalCode,
            address_id = addressId
        )
        val wishlistParam = WishlistV2Params(
            source = "cart",
            wishlistChosenAddress = WishlistV2Params.WishlistChosenAddress(
                districtId = districtId,
                cityId = cityId,
                latitude = lat,
                longitude = long,
                postalCode = postalCode,
                addressId = addressId
            )
        )

        every { getWishlistV2UseCase.setParams(wishlistParam) } just Runs
        coEvery { getWishlistV2UseCase.executeOnBackground() } returns Success(wishlistV2)

        cartListPresenter.setLocalizingAddressData(lca)
        cartListPresenter.processGetWishlistV2Data()
        cartListPresenter.detachView()

        verify { getWishlistV2UseCase.setParams(wishlistParam) }
        coVerify { getWishlistV2UseCase.executeOnBackground() }
    }
}
