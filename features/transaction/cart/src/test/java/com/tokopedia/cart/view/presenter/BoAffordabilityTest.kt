package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.data.model.request.CartShopGroupTickerAggregatorParam
import com.tokopedia.cart.data.model.response.cartshoptickeraggregator.CartShopGroupTickerAggregatorData
import com.tokopedia.cart.data.model.response.cartshoptickeraggregator.CartShopGroupTickerAggregatorResponse
import com.tokopedia.cart.data.model.response.cartshoptickeraggregator.CartShopGroupTickerAggregatorTicker
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class BoAffordabilityTest : BaseCartTest() {

    @Test
    fun `WHEN shop products is overweight THEN should not hit API`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString,
            maximumShippingWeight = 1.0,
            maximumWeightWording = "overweight",
            isAllSelected = true,
            productUiModelList = arrayListOf(
                CartItemHolderData(
                    isSelected = true,
                    quantity = 10,
                    productWeight = 1000000
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            cartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
                state = CartShopGroupTickerState.FAILED
            )
            view.updateCartShopGroupTicker(cartShopHolderData)
        }

        coVerify(inverse = true) {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN shop products is not overweight THEN should hit API`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString,
            maximumShippingWeight = 1000.0,
            maximumWeightWording = "overweight",
            isAllSelected = true,
            productUiModelList = arrayListOf(
                CartItemHolderData(
                    productId = "111",
                    cartId = "111",
                    isSelected = true,
                    quantity = 10,
                    productWeight = 1
                )
            )
        )
        val ticker = "dapat bebas ongkir"

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 0L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = ticker
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            cartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
                state = CartShopGroupTickerState.SUCCESS_AFFORD,
                tickerText = ticker
            )
            view.updateCartShopGroupTicker(cartShopHolderData)
        }

        coVerify {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN bo affordability success not afford THEN should show not afford ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 1_000L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = ticker
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            cartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
                state = CartShopGroupTickerState.SUCCESS_NOT_AFFORD,
                tickerText = ticker
            )
            view.updateCartShopGroupTicker(cartShopHolderData)
        }
    }

    @Test
    fun `WHEN bo affordability success afford THEN should show afford ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString
        )
        val ticker = "dapat bebas ongkir"

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 0L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = ticker
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            cartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
                state = CartShopGroupTickerState.SUCCESS_AFFORD,
                tickerText = ticker
            )
            view.updateCartShopGroupTicker(cartShopHolderData)
        }
    }

    @Test
    fun `WHEN bo affordability response empty THEN should not show ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString
        )
        val ticker = ""

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 0L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = ticker
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            cartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
                state = CartShopGroupTickerState.EMPTY,
                tickerText = ticker
            )
            view.updateCartShopGroupTicker(cartShopHolderData)
        }
    }

    @Test
    fun `WHEN get bo affordability failed THEN should show failed ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString
        )

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } throws IOException()

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            cartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
                state = CartShopGroupTickerState.FAILED
            )
            view.updateCartShopGroupTicker(cartShopHolderData)
        }
    }

    @Test
    fun `WHEN get bo affordability twice in debounce time THEN should only hit API once`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 1_000L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = ticker
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceTimeBy(1)
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN get bo affordability and view detached THEN should not hit API and not update view`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 1_000L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = ticker
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceTimeBy(1)
        cartListPresenter.detachView()
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        coVerify(inverse = true) {
            cartShopGroupTickerAggregatorUseCase(any())
            view.updateCartShopGroupTicker(any())
        }
    }

    @Test
    fun `WHEN get bo affordability with snippet LCA THEN should hit with only snippet data LCA`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        val slotParam = slot<CartShopGroupTickerAggregatorParam>()
        coEvery {
            cartShopGroupTickerAggregatorUseCase(capture(slotParam))
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 1_000L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = ticker
                )
            )
        )

        val lca = LocalCacheModel(
            district_id = "123",
            postal_code = "123"
        )

        // WHEN
        cartListPresenter.setLocalizingAddressData(lca)
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        assertEquals("123|123|,", slotParam.captured.ratesParam.destination)
    }

    @Test
    fun `WHEN get bo affordability with full LCA THEN should hit with only full data LCA`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        val slotParam = slot<CartShopGroupTickerAggregatorParam>()
        coEvery {
            cartShopGroupTickerAggregatorUseCase(capture(slotParam))
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 1_000L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = ticker
                )
            )
        )

        val lca = LocalCacheModel(
            address_id = "123",
            district_id = "123",
            postal_code = "123",
            lat = "123",
            long = "123"
        )

        // WHEN
        cartListPresenter.setLocalizingAddressData(lca)
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        assertEquals("123|123|123,123", slotParam.captured.ratesParam.destination)
    }
}
