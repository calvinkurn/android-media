package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.view.uimodel.CartBuyAgainHolderData
import com.tokopedia.cart.view.uimodel.CartBuyAgainItemHolderData
import com.tokopedia.cart.view.uimodel.CartBuyAgainViewAllData
import com.tokopedia.cart.view.uimodel.CartEmptyHolderData
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cart.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cart.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cart.view.uimodel.DisabledReasonHolderData
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class CartBuyAgainTest : BaseCartViewModelTest() {

    companion object {
        private const val BUY_AGAIN_PRODUCTS_LIMIT = 6
    }

    @Test
    fun `WHEN get buy again success THEN should render buy again section`() {
        // GIVEN
        cartViewModel.cartDataList.value = arrayListOf(
            CartEmptyHolderData(),
            CartGroupHolderData(),
            CartItemHolderData(),
            CartShopBottomHolderData(shopData = CartGroupHolderData()),
            ShipmentSellerCashbackModel(),
            DisabledItemHeaderHolderData(),
            DisabledReasonHolderData(),
            DisabledAccordionHolderData()
        )
        val cartSize = cartViewModel.cartDataList.value.size

        val response = mutableListOf<RecommendationWidget>().apply {
            add(
                RecommendationWidget(
                    recommendationItemList = listOf(
                        RecommendationItem()
                    )
                )
            )
        }

        coEvery { getRecommendationUseCase.getData(any()) } returns response

        // WHEN
        cartViewModel.processGetBuyAgainData()

        // THEN
        Assert.assertEquals(
            cartSize + 2,
            cartViewModel.cartDataList.value.size
        )
        Assert.assertTrue(
            cartViewModel.cartDataList.value[cartSize] is CartSectionHeaderHolderData
        )
        Assert.assertTrue(
            cartViewModel.cartDataList.value[cartSize + 1] is CartBuyAgainHolderData
        )
    }

    @Test
    fun `WHEN get buy again and more than 6 THEN should render maximum product size`() {
        // GIVEN
        cartViewModel.cartDataList.value = arrayListOf(
            CartEmptyHolderData(),
            CartGroupHolderData(),
            CartItemHolderData(),
            CartShopBottomHolderData(shopData = CartGroupHolderData()),
            ShipmentSellerCashbackModel(),
            DisabledItemHeaderHolderData(),
            DisabledReasonHolderData(),
            DisabledAccordionHolderData()
        )
        val cartSize = cartViewModel.cartDataList.value.size

        val response = mutableListOf<RecommendationWidget>().apply {
            add(
                RecommendationWidget(
                    seeMoreAppLink = "https://example.com",
                    recommendationItemList = listOf(
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem(),
                        RecommendationItem()
                    )
                )
            )
        }

        coEvery { getRecommendationUseCase.getData(any()) } returns response

        // WHEN
        cartViewModel.processGetBuyAgainData()

        // THEN
        Assert.assertTrue(
            cartViewModel.cartDataList.value[cartSize] is CartSectionHeaderHolderData
        )
        val cartBuyAgainHolderData =
            cartViewModel.cartDataList.value[cartSize + 1] as? CartBuyAgainHolderData
        Assert.assertNotNull(cartBuyAgainHolderData)
        val buyAgainItems = cartBuyAgainHolderData!!.buyAgainList
            .filterIsInstance<CartBuyAgainItemHolderData>()
        Assert.assertTrue(buyAgainItems.size == BUY_AGAIN_PRODUCTS_LIMIT)
        Assert.assertTrue(cartBuyAgainHolderData.buyAgainList.last() is CartBuyAgainViewAllData)
    }

    @Test
    fun `WHEN get buy again success but empty THEN should not render buy again section`() {
        // GIVEN
        cartViewModel.cartDataList.value = arrayListOf(
            CartEmptyHolderData(),
            CartGroupHolderData(),
            CartItemHolderData(),
            CartShopBottomHolderData(shopData = CartGroupHolderData()),
            ShipmentSellerCashbackModel(),
            DisabledItemHeaderHolderData(),
            DisabledReasonHolderData(),
            DisabledAccordionHolderData()
        )
        val cartSizeBeforeGetBuyAgain = cartViewModel.cartDataList.value.size

        val response = mutableListOf<RecommendationWidget>().apply {
            add(
                RecommendationWidget(
                    recommendationItemList = listOf()
                )
            )
        }

        coEvery { getRecommendationUseCase.getData(any()) } returns response

        // WHEN
        cartViewModel.processGetBuyAgainData()

        // THEN
        Assert.assertEquals(
            cartSizeBeforeGetBuyAgain,
            cartViewModel.cartDataList.value.size
        )
    }

    @Test
    fun `WHEN get buy again failed THEN should not render buy again section`() {
        // GIVEN
        cartViewModel.cartDataList.value = arrayListOf(
            CartEmptyHolderData(),
            CartGroupHolderData(),
            CartItemHolderData(),
            CartShopBottomHolderData(shopData = CartGroupHolderData()),
            ShipmentSellerCashbackModel(),
            DisabledItemHeaderHolderData(),
            DisabledReasonHolderData(),
            DisabledAccordionHolderData()
        )
        val cartSize = cartViewModel.cartDataList.value.size

        coEvery { getRecommendationUseCase.getData(any()) } throws CartResponseErrorException()

        // WHEN
        cartViewModel.processGetBuyAgainData()

        // THEN
        Assert.assertEquals(cartSize, cartViewModel.cartDataList.value.size)
    }

    @Test
    fun `WHEN update buy again floating button to true THEN should update isVisible should true`() {
        // GIVEN
        val isVisible = true
        val response = mutableListOf<RecommendationWidget>().apply {
            add(
                RecommendationWidget(
                    recommendationItemList = listOf(
                        RecommendationItem()
                    )
                )
            )
        }
        coEvery { getRecommendationUseCase.getData(any()) } returns response
        cartViewModel.processGetBuyAgainData()

        // WHEN
        cartViewModel.updateBuyAgainFloatingButtonVisibility(isVisible)

        // THEN
        Assert.assertTrue(cartViewModel.buyAgainFloatingButtonData.getOrAwaitValue().isVisible)
    }

    @Test
    fun `WHEN update buy again floating button to false THEN should update isVisible should false`() {
        // GIVEN
        val isVisible = false
        val response = mutableListOf<RecommendationWidget>().apply {
            add(
                RecommendationWidget(
                    recommendationItemList = listOf(
                        RecommendationItem()
                    )
                )
            )
        }
        coEvery { getRecommendationUseCase.getData(any()) } returns response
        cartViewModel.processGetBuyAgainData()

        // WHEN
        cartViewModel.updateBuyAgainFloatingButtonVisibility(isVisible)

        // THEN
        Assert.assertTrue(!cartViewModel.buyAgainFloatingButtonData.getOrAwaitValue().isVisible)
    }
}
