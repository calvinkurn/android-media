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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class CartShopGroupTickerTest : BaseCartTest() {

    companion object {

        fun selectedProductWithoutBundle() = CartItemHolderData(
            productId = "111",
            cartId = "111",
            isSelected = true,
            isBundlingItem = false,
            bundleIds = emptyList()
        )

        fun unselectedProductWithoutBundle() = CartItemHolderData(
            productId = "222",
            cartId = "222",
            isSelected = false,
            isBundlingItem = false,
            bundleIds = emptyList()
        )

        fun selectedProductWithBundle() = CartItemHolderData(
            productId = "555",
            cartId = "555",
            isSelected = true,
            isBundlingItem = false,
            bundleIds = listOf("356", "467")
        )

        fun unselectedProductWithBundle() = CartItemHolderData(
            productId = "666",
            cartId = "666",
            isSelected = false,
            isBundlingItem = false,
            bundleIds = listOf("578", "689")
        )

        fun selectedBundleProduct() = CartItemHolderData(
            productId = "333",
            cartId = "333",
            isSelected = true,
            isBundlingItem = true,
            bundleIds = listOf("134", "245")
        )

        fun unselectedBundleProduct() = CartItemHolderData(
            productId = "444",
            cartId = "444",
            isSelected = false,
            isBundlingItem = true,
            bundleIds = listOf("123", "234")
        )
    }

    @Test
    fun `WHEN bo affordability and bundling is disabled THEN should not hit API`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartShopHolderData = CartShopHolderData(
            cartString = cartString,
            maximumShippingWeight = 1000.0,
            maximumWeightWording = "overweight",
            isAllSelected = true,
            productUiModelList = arrayListOf(
                CartItemHolderData(
                    isSelected = true,
                    quantity = 1,
                    productWeight = 1
                )
            )
        )
        val expectedCartShopHolderData = cartShopHolderData.copy()

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            expectedCartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
                state = CartShopGroupTickerState.EMPTY
            )
            view.updateCartShopGroupTicker(expectedCartShopHolderData)
        }
        coVerify(inverse = true) {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN shop product is overweight but bo affordability disabled and bundling enabled THEN should hit cart aggregator API`() {
        // GIVEN
        val cartString = "123-123-123"
        val tickerData = CartShopGroupTickerData(
            enableCartAggregator = true,
            enableBoAffordability = false
        )
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = tickerData,
            cartString = cartString,
            maximumShippingWeight = 1.0,
            maximumWeightWording = "overweight",
            isAllSelected = true,
            productUiModelList = arrayListOf(
                CartItemHolderData(
                    productId = "111",
                    cartId = "111",
                    isSelected = true,
                    quantity = 10,
                    productWeight = 1000000,
                    isBundlingItem = false,
                    bundleIds = listOf("123", "234")
                )
            )
        )
        val tickerText = "dapat bebas ongkir"
        val expectedCartShopHolderData = cartShopHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 0L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = tickerText
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
                state = CartShopGroupTickerState.SUCCESS_AFFORD,
                tickerText = tickerText
            )
            view.updateCartShopGroupTicker(expectedCartShopHolderData)
        }

        coVerify {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN shop products is overweight THEN should not hit cart aggregator API`() {
        // GIVEN
        val cartString = "123-123-123"
        val tickerData = CartShopGroupTickerData(
            enableBoAffordability = true
        )
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = tickerData,
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
        val expectedCartShopHolderData = cartShopHolderData.copy()

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
                state = CartShopGroupTickerState.FAILED
            )
            view.updateCartShopGroupTicker(expectedCartShopHolderData)
        }

        coVerify(inverse = true) {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN shop products is not overweight THEN should hit cart aggregator API`() {
        // GIVEN
        val cartString = "123-123-123"
        val tickerData = CartShopGroupTickerData(
            enableBoAffordability = true
        )
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = tickerData,
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
        val tickerText = "dapat bebas ongkir"
        val expectedCartShopHolderData = cartShopHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 0L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = tickerText
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
                state = CartShopGroupTickerState.SUCCESS_AFFORD,
                tickerText = tickerText
            )
            view.updateCartShopGroupTicker(expectedCartShopHolderData)
        }

        coVerify {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN bo affordability success not afford THEN should show not afford ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val tickerData = CartShopGroupTickerData(
            enableBoAffordability = true
        )
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = tickerData,
            cartString = cartString
        )
        val tickerText = "+ Rp10.000 lagi untuk dapat bebas ongkir"
        val expectedCartShopHolderData = cartShopHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 1_000L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = tickerText
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
                state = CartShopGroupTickerState.SUCCESS_NOT_AFFORD,
                tickerText = tickerText
            )
            view.updateCartShopGroupTicker(expectedCartShopHolderData)
        }
    }

    @Test
    fun `WHEN bo affordability success afford THEN should show afford ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val tickerData = CartShopGroupTickerData(
            enableBoAffordability = true
        )
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = tickerData,
            cartString = cartString
        )
        val tickerText = "dapat bebas ongkir"
        val expectedCartShopHolderData = cartShopHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 0L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = tickerText
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
                state = CartShopGroupTickerState.SUCCESS_AFFORD,
                tickerText = tickerText
            )
            view.updateCartShopGroupTicker(expectedCartShopHolderData)
        }
    }

    @Test
    fun `WHEN bo affordability response empty THEN should not show ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val tickerData = CartShopGroupTickerData(
            enableBoAffordability = true
        )
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = tickerData,
            cartString = cartString
        )
        val tickerText = ""
        val expectedCartShopHolderData = cartShopHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorResponse(
            CartShopGroupTickerAggregatorData(
                minTransaction = 0L,
                ticker = CartShopGroupTickerAggregatorTicker(
                    text = tickerText
                )
            )
        )

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
                state = CartShopGroupTickerState.EMPTY,
                tickerText = tickerText
            )
            view.updateCartShopGroupTicker(expectedCartShopHolderData)
        }
    }

    @Test
    fun `WHEN get bo affordability failed THEN should show failed ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val tickerData = CartShopGroupTickerData(
            enableBoAffordability = true
        )
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = tickerData,
            cartString = cartString
        )
        val expectedCartShopHolderData = cartShopHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } throws IOException()

        // WHEN
        cartListPresenter.checkCartShopGroupTicker(cartShopHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        verify {
            expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
                state = CartShopGroupTickerState.FAILED
            )
            view.updateCartShopGroupTicker(expectedCartShopHolderData)
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

    @Test
    fun `WHEN cart aggregator disabled with valid product THEN bundle cross sell should be disabled`() {
        // GIVEN
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(
                enableCartAggregator = false
            ),
            productUiModelList = arrayListOf(
                unselectedProductWithoutBundle(),
                unselectedProductWithBundle(),
                unselectedBundleProduct(),
                selectedProductWithoutBundle(),
                selectedProductWithBundle()
            )
        )

        // WHEN
        val enableBundleCrossSell = cartListPresenter.checkEnableBundleCrossSell(cartShopHolderData)

        // THEN
        assertFalse(enableBundleCrossSell)
    }

    @Test
    fun `WHEN cart aggregator enabled with valid product THEN bundle cross sell should be enabled`() {
        // GIVEN
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(
                enableCartAggregator = true
            ),
            productUiModelList = arrayListOf(
                unselectedProductWithoutBundle(),
                unselectedProductWithBundle(),
                unselectedBundleProduct(),
                selectedProductWithoutBundle(),
                selectedProductWithBundle()
            )
        )

        // WHEN
        val enableBundleCrossSell = cartListPresenter.checkEnableBundleCrossSell(cartShopHolderData)

        // THEN
        assertTrue(enableBundleCrossSell)
    }

    @Test
    fun `WHEN cart aggregator enabled with selected bundle product THEN bundle cross sell should be disabled`() {
        // GIVEN
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(
                enableCartAggregator = true
            ),
            productUiModelList = arrayListOf(
                unselectedProductWithoutBundle(),
                unselectedProductWithBundle(),
                unselectedBundleProduct(),
                selectedProductWithoutBundle(),
                selectedProductWithBundle(),
                selectedBundleProduct()
            )
        )

        // WHEN
        val enableBundleCrossSell = cartListPresenter.checkEnableBundleCrossSell(cartShopHolderData)

        // THEN
        assertFalse(enableBundleCrossSell)
    }

    @Test
    fun `WHEN cart aggregator enabled with no selected product THEN bundle cross sell should be disabled`() {
        // GIVEN
        val cartShopHolderData = CartShopHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(
                enableCartAggregator = true
            ),
            productUiModelList = arrayListOf(
                unselectedProductWithoutBundle(),
                unselectedProductWithBundle(),
                unselectedBundleProduct()
            )
        )

        // WHEN
        val enableBundleCrossSell = cartListPresenter.checkEnableBundleCrossSell(cartShopHolderData)

        // THEN
        assertFalse(enableBundleCrossSell)
    }
}
