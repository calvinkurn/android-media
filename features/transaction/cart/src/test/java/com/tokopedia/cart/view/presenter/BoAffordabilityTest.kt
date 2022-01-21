package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopBoAffordabilityData
import com.tokopedia.cart.view.uimodel.CartShopBoAffordabilityState
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityResponse
import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityTexts
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        cartListPresenter.checkBoAffordability(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            cartShopHolderData.boAffordability = CartShopBoAffordabilityData(
                state = CartShopBoAffordabilityState.FAILED
            )
            view.updateCartBoAffordability(cartShopHolderData)
        }

        coVerify(inverse = true) {
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
        } returns BoAffordabilityResponse(
            1_000, BoAffordabilityTexts(
                tickerCart = ticker
            )
        )

        // WHEN
        cartListPresenter.checkBoAffordability(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            cartShopHolderData.boAffordability = CartShopBoAffordabilityData(
                state = CartShopBoAffordabilityState.SUCCESS_NOT_AFFORD,
                tickerText = ticker
            )
            view.updateCartBoAffordability(cartShopHolderData)
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
        } returns BoAffordabilityResponse(
            0, BoAffordabilityTexts(
                tickerCart = ticker
            )
        )

        // WHEN
        cartListPresenter.checkBoAffordability(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            cartShopHolderData.boAffordability = CartShopBoAffordabilityData(
                state = CartShopBoAffordabilityState.SUCCESS_AFFORD,
                tickerText = ticker
            )
            view.updateCartBoAffordability(cartShopHolderData)
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
        cartListPresenter.checkBoAffordability(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            cartShopHolderData.boAffordability = CartShopBoAffordabilityData(
                state = CartShopBoAffordabilityState.FAILED
            )
            view.updateCartBoAffordability(cartShopHolderData)
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
        } returns BoAffordabilityResponse(
            1_000, BoAffordabilityTexts(
                tickerCart = ticker
            )
        )

        // WHEN
        cartListPresenter.checkBoAffordability(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceTimeBy(1)
        cartListPresenter.checkBoAffordability(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) {
            boAffordabilityUseCase.setParam(any()).executeOnBackground()
        }
    }
}