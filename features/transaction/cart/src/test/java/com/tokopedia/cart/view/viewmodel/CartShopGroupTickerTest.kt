package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.data.model.request.CartShopGroupTickerAggregatorParam
import com.tokopedia.cart.data.model.response.cartshoptickeraggregator.CartShopGroupTickerAggregatorBundleBottomSheet
import com.tokopedia.cart.data.model.response.cartshoptickeraggregator.CartShopGroupTickerAggregatorData
import com.tokopedia.cart.data.model.response.cartshoptickeraggregator.CartShopGroupTickerAggregatorGqlResponse
import com.tokopedia.cart.data.model.response.cartshoptickeraggregator.CartShopGroupTickerAggregatorResponse
import com.tokopedia.cart.data.model.response.cartshoptickeraggregator.CartShopGroupTickerAggregatorTicker
import com.tokopedia.cartrevamp.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.cartrevamp.view.uimodel.CartGlobalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopGroupTickerData
import com.tokopedia.cartrevamp.view.uimodel.CartShopGroupTickerState
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class CartShopGroupTickerTest : BaseCartViewModelTest() {

    companion object {

        fun selectedProductWithoutBundle() = CartItemHolderData(
            productId = "111",
            cartId = "111",
            isSelected = true,
            isBundlingItem = false,
            bundleIds = emptyList(),
            quantity = 1,
            productWeight = 1
        )

        fun unselectedProductWithoutBundle() = CartItemHolderData(
            productId = "222",
            cartId = "222",
            isSelected = false,
            isBundlingItem = false,
            bundleIds = emptyList(),
            quantity = 1,
            productWeight = 1
        )

        fun selectedProductWithBundle() = CartItemHolderData(
            productId = "555",
            cartId = "555",
            isSelected = true,
            isBundlingItem = false,
            bundleIds = listOf("356", "467"),
            quantity = 1,
            productWeight = 1
        )

        fun unselectedProductWithBundle() = CartItemHolderData(
            productId = "666",
            cartId = "666",
            isSelected = false,
            isBundlingItem = false,
            bundleIds = listOf("578", "689"),
            quantity = 1,
            productWeight = 1
        )

        fun selectedBundleProduct() = CartItemHolderData(
            productId = "333",
            cartId = "333",
            isSelected = true,
            isBundlingItem = true,
            bundleIds = listOf("134", "245"),
            quantity = 1,
            productWeight = 1
        )

        fun unselectedBundleProduct() = CartItemHolderData(
            productId = "444",
            cartId = "444",
            isSelected = false,
            isBundlingItem = true,
            bundleIds = listOf("123", "234"),
            quantity = 1,
            productWeight = 1
        )

        fun selectedBundleProductWithoutBundleIds() = CartItemHolderData(
            productId = "999",
            cartId = "999",
            isSelected = true,
            isBundlingItem = true,
            bundleIds = emptyList(),
            quantity = 1,
            productWeight = 1
        )

        fun unselectedBundleProductWithoutBundleIds() = CartItemHolderData(
            productId = "998",
            cartId = "998",
            isSelected = false,
            isBundlingItem = true,
            bundleIds = emptyList(),
            quantity = 1,
            productWeight = 1
        )
    }

    @Test
    fun `WHEN bo affordability and bundling is disabled THEN should not hit API`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartGroupHolderData = CartGroupHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(
                enableCartAggregator = false,
                enableBoAffordability = false
            ),
            cartString = cartString,
            maximumShippingWeight = 1000.0,
            maximumWeightWording = "overweight",
            isAllSelected = true,
            productUiModelList = arrayListOf(
                selectedProductWithBundle()
            )
        )
        val expectedCartShopHolderData = cartGroupHolderData.copy()

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
            enableCartAggregator = false,
            enableBoAffordability = false,
            enableBundleCrossSell = false,
            state = CartShopGroupTickerState.EMPTY
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )
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
        val cartGroupHolderData = CartGroupHolderData(
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
        val expectedCartShopHolderData = cartGroupHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorGqlResponse(
            CartShopGroupTickerAggregatorResponse(
                CartShopGroupTickerAggregatorData(
                    minTransaction = 0L,
                    ticker = CartShopGroupTickerAggregatorTicker(
                        text = tickerText
                    )
                )
            )
        )

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
            state = CartShopGroupTickerState.SUCCESS_AFFORD,
            tickerText = tickerText
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )

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
        val cartGroupHolderData = CartGroupHolderData(
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
        val expectedCartShopHolderData = cartGroupHolderData.copy()

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
            state = CartShopGroupTickerState.FAILED
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )

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
        val cartGroupHolderData = CartGroupHolderData(
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
        val expectedCartShopHolderData = cartGroupHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorGqlResponse(
            CartShopGroupTickerAggregatorResponse(
                CartShopGroupTickerAggregatorData(
                    minTransaction = 0L,
                    ticker = CartShopGroupTickerAggregatorTicker(
                        text = tickerText
                    )
                )
            )
        )

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
            state = CartShopGroupTickerState.SUCCESS_AFFORD,
            tickerText = tickerText
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )

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
        val cartGroupHolderData = CartGroupHolderData(
            cartShopGroupTicker = tickerData,
            cartString = cartString
        )
        val tickerText = "+ Rp10.000 lagi untuk dapat bebas ongkir"
        val expectedCartShopHolderData = cartGroupHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorGqlResponse(
            CartShopGroupTickerAggregatorResponse(
                CartShopGroupTickerAggregatorData(
                    minTransaction = 1_000L,
                    ticker = CartShopGroupTickerAggregatorTicker(
                        text = tickerText
                    )
                )
            )
        )

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
            state = CartShopGroupTickerState.SUCCESS_NOT_AFFORD,
            tickerText = tickerText
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )
    }

    @Test
    fun `WHEN bo affordability success afford THEN should show afford ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val tickerData = CartShopGroupTickerData(
            enableBoAffordability = true
        )
        val cartGroupHolderData = CartGroupHolderData(
            cartShopGroupTicker = tickerData,
            cartString = cartString
        )
        val tickerText = "dapat bebas ongkir"
        val expectedCartShopHolderData = cartGroupHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorGqlResponse(
            CartShopGroupTickerAggregatorResponse(
                CartShopGroupTickerAggregatorData(
                    minTransaction = 0L,
                    ticker = CartShopGroupTickerAggregatorTicker(
                        text = tickerText
                    )
                )
            )
        )

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
            state = CartShopGroupTickerState.SUCCESS_AFFORD,
            tickerText = tickerText
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )
    }

    @Test
    fun `WHEN bo affordability response empty THEN should not show ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val tickerData = CartShopGroupTickerData(
            enableBoAffordability = true
        )
        val cartGroupHolderData = CartGroupHolderData(
            cartShopGroupTicker = tickerData,
            cartString = cartString
        )
        val tickerText = ""
        val expectedCartShopHolderData = cartGroupHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorGqlResponse(
            CartShopGroupTickerAggregatorResponse(
                CartShopGroupTickerAggregatorData(
                    minTransaction = 0L,
                    ticker = CartShopGroupTickerAggregatorTicker(
                        text = tickerText
                    )
                )
            )
        )

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
            state = CartShopGroupTickerState.EMPTY,
            tickerText = tickerText
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )
    }

    @Test
    fun `WHEN get bo affordability failed THEN should show failed ticker`() {
        // GIVEN
        val cartString = "123-123-123"
        val tickerData = CartShopGroupTickerData(
            enableBoAffordability = true
        )
        val cartGroupHolderData = CartGroupHolderData(
            cartShopGroupTicker = tickerData,
            cartString = cartString
        )
        val expectedCartShopHolderData = cartGroupHolderData.copy()

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } throws IOException()

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = tickerData.copy(
            state = CartShopGroupTickerState.FAILED
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )
    }

    @Test
    fun `WHEN get bo affordability twice in debounce time THEN should only hit API once`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartGroupHolderData = CartGroupHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorGqlResponse(
            CartShopGroupTickerAggregatorResponse(
                CartShopGroupTickerAggregatorData(
                    minTransaction = 1_000L,
                    ticker = CartShopGroupTickerAggregatorTicker(
                        text = ticker
                    )
                )
            )
        )

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceTimeBy(1)
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN get bo affordability with snippet LCA THEN should hit with only snippet data LCA`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartGroupHolderData = CartGroupHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        val slotParam = slot<CartShopGroupTickerAggregatorParam>()
        coEvery {
            cartShopGroupTickerAggregatorUseCase(capture(slotParam))
        } returns CartShopGroupTickerAggregatorGqlResponse(
            CartShopGroupTickerAggregatorResponse(
                CartShopGroupTickerAggregatorData(
                    minTransaction = 1_000L,
                    ticker = CartShopGroupTickerAggregatorTicker(
                        text = ticker
                    )
                )
            )
        )

        val lca = LocalCacheModel(
            district_id = "123",
            postal_code = "123"
        )

        // WHEN
        cartViewModel.setLocalizingAddressData(lca)
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        assertEquals("123|123|,", slotParam.captured.ratesParam.destination)
    }

    @Test
    fun `WHEN get bo affordability with full LCA THEN should hit with only full data LCA`() {
        // GIVEN
        val cartString = "123-123-123"
        val cartGroupHolderData = CartGroupHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(enableBoAffordability = true),
            cartString = cartString
        )
        val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

        val slotParam = slot<CartShopGroupTickerAggregatorParam>()
        coEvery {
            cartShopGroupTickerAggregatorUseCase(capture(slotParam))
        } returns CartShopGroupTickerAggregatorGqlResponse(
            CartShopGroupTickerAggregatorResponse(
                CartShopGroupTickerAggregatorData(
                    minTransaction = 1_000L,
                    ticker = CartShopGroupTickerAggregatorTicker(
                        text = ticker
                    )
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
        cartViewModel.setLocalizingAddressData(lca)
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        assertEquals("123|123|123,123", slotParam.captured.ratesParam.destination)
    }

    @Test
    fun `WHEN cart aggregator disabled with valid product THEN bundle cross sell should be disabled`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(
            cartString = "123-123-123",
            cartShopGroupTicker = CartShopGroupTickerData(
                enableCartAggregator = false,
                enableBoAffordability = false
            ),
            productUiModelList = arrayListOf(
                unselectedProductWithoutBundle(),
                unselectedProductWithBundle(),
                unselectedBundleProduct(),
                unselectedBundleProductWithoutBundleIds(),
                selectedProductWithoutBundle(),
                selectedProductWithBundle(),
                selectedBundleProductWithoutBundleIds()
            )
        )
        val expectedCartShopHolderData = cartGroupHolderData.copy()

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
            enableCartAggregator = false,
            enableBoAffordability = false,
            enableBundleCrossSell = false,
            state = CartShopGroupTickerState.EMPTY
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )

        coVerify(inverse = true) {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN cart aggregator enabled with valid product THEN bundle cross sell should be enabled`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(
            cartString = "123-123-123",
            cartShopGroupTicker = CartShopGroupTickerData(
                enableCartAggregator = true,
                enableBoAffordability = false
            ),
            productUiModelList = arrayListOf(
                unselectedProductWithoutBundle(),
                unselectedProductWithBundle(),
                unselectedBundleProduct(),
                unselectedBundleProductWithoutBundleIds(),
                selectedProductWithoutBundle(),
                selectedProductWithBundle(),
                selectedBundleProductWithoutBundleIds()
            )
        )
        val tickerText = "tambah produk bundling"
        val expectedCartShopHolderData = cartGroupHolderData.copy()
        coEvery {
            cartShopGroupTickerAggregatorUseCase(any())
        } returns CartShopGroupTickerAggregatorGqlResponse(
            CartShopGroupTickerAggregatorResponse(
                CartShopGroupTickerAggregatorData(
                    minTransaction = 0L,
                    ticker = CartShopGroupTickerAggregatorTicker(
                        text = tickerText
                    ),
                    bundleBottomSheet = CartShopGroupTickerAggregatorBundleBottomSheet(
                        bundleIds = listOf("123", "234")
                    )
                )
            )
        )

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
            enableCartAggregator = true,
            enableBoAffordability = false,
            enableBundleCrossSell = true,
            state = CartShopGroupTickerState.SUCCESS_AFFORD,
            tickerText = tickerText,
            cartBundlingBottomSheetData = CartBundlingBottomSheetData(
                bundleIds = listOf("123", "234")
            )
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )
        coVerify {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN cart aggregator enabled with selected bundle product THEN bundle cross sell should be disabled`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(
            cartString = "123-123-123",
            cartShopGroupTicker = CartShopGroupTickerData(
                enableCartAggregator = true,
                enableBoAffordability = false
            ),
            productUiModelList = arrayListOf(
                unselectedProductWithoutBundle(),
                unselectedProductWithBundle(),
                unselectedBundleProduct(),
                unselectedBundleProductWithoutBundleIds(),
                selectedBundleProductWithoutBundleIds(),
                selectedProductWithoutBundle(),
                selectedProductWithBundle(),
                selectedBundleProduct()
            )
        )
        val expectedCartShopHolderData = cartGroupHolderData.copy()

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
            enableCartAggregator = true,
            enableBoAffordability = false,
            enableBundleCrossSell = false,
            state = CartShopGroupTickerState.EMPTY
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )
        coVerify(inverse = true) {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN cart aggregator enabled with no selected product THEN bundle cross sell should be disabled`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(
            cartString = "123-123-123",
            cartShopGroupTicker = CartShopGroupTickerData(
                enableCartAggregator = true,
                enableBoAffordability = false
            ),
            productUiModelList = arrayListOf(
                unselectedProductWithoutBundle(),
                unselectedProductWithBundle(),
                unselectedBundleProduct(),
                unselectedBundleProductWithoutBundleIds()
            )
        )
        val expectedCartShopHolderData = cartGroupHolderData.copy()

        // WHEN
        cartViewModel.checkCartShopGroupTicker(cartGroupHolderData)
        coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()

        // THEN
        expectedCartShopHolderData.cartShopGroupTicker = CartShopGroupTickerData(
            enableCartAggregator = true,
            enableBoAffordability = false,
            enableBundleCrossSell = false,
            state = CartShopGroupTickerState.EMPTY
        )
        assertEquals(
            CartGlobalEvent.UpdateCartShopGroupTicker(expectedCartShopHolderData),
            cartViewModel.globalEvent.value
        )
        coVerify(inverse = true) {
            cartShopGroupTickerAggregatorUseCase(any())
        }
    }

    @Test
    fun `WHEN cart data product is empty THEN return false for bundle cross sell`() {
        // Given
        val cartGroupHolderData = CartGroupHolderData(
            cartShopGroupTicker = CartShopGroupTickerData(
                enableCartAggregator = true,
                enableBoAffordability = true
            ),
            productUiModelList = arrayListOf()
        )

        // When
        val enableBundleCrossSell = cartViewModel.checkEnableBundleCrossSell(cartGroupHolderData)

        // Then
        assertFalse(enableBundleCrossSell)
    }
}
