package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityDataResponse
import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityTexts
import com.tokopedia.logisticcart.shipping.model.RatesParam
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
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN shop products is not overweight THEN should hit API`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
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
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
        } returns BoAffordabilityDataResponse(
            0, BoAffordabilityTexts(
                tickerCart = ticker
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
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN bo affordability success not afford THEN should show not afford ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"


        coEvery {
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
        } returns BoAffordabilityDataResponse(
            1_000, BoAffordabilityTexts(
                tickerCart = ticker
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
            cartString = cartString
        )
        val ticker = "dapat bebas ongkir"

        coEvery {
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
        } returns BoAffordabilityDataResponse(
            0, BoAffordabilityTexts(
                tickerCart = ticker
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
            cartString = cartString
        )
        val ticker = ""

        coEvery {
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
        } returns BoAffordabilityDataResponse(
            0, BoAffordabilityTexts(
                tickerCart = ticker
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
            cartString = cartString
        )

        coEvery {
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
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
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        coEvery {
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
        } returns BoAffordabilityDataResponse(
            1_000, BoAffordabilityTexts(
                tickerCart = ticker
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceTimeBy(1)
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) {
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN get bo affordability and view detached THEN should not hit API and not update view`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        coEvery {
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
        } returns BoAffordabilityDataResponse(
            1_000, BoAffordabilityTexts(
                tickerCart = ticker
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceTimeBy(1)
        cartListPresenter.detachView()
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        coVerify(inverse = true) {
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
            view.updateCartShopGroupTicker(any())
        }
    }

    @Test
    fun `WHEN get bo affordability with snippet LCA THEN should hit with only snippet data LCA`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        val slotParam = slot<RatesParam>()
        coEvery {
            boAffordabilityUseCase.setParam(capture(slotParam)).executeOnBackground()
        } returns BoAffordabilityDataResponse(
            1_000, BoAffordabilityTexts(
                tickerCart = ticker
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
        assertEquals("123|123|,", slotParam.captured.destination)
    }

    @Test
    fun `WHEN get bo affordability with full LCA THEN should hit with only full data LCA`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        val slotParam = slot<RatesParam>()
        coEvery {
            boAffordabilityUseCase.setParam(capture(slotParam)).executeOnBackground()
        } returns BoAffordabilityDataResponse(
            1_000, BoAffordabilityTexts(
                tickerCart = ticker
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
        assertEquals("123|123|123,123", slotParam.captured.destination)
    }
}
