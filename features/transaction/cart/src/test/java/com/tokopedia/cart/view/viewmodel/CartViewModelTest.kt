package com.tokopedia.cart.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cartrevamp.view.uimodel.CartGlobalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledReasonHolderData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CartViewModelTest : BaseCartViewModelTest() {

    private lateinit var cartGlobalEventObserver: Observer<CartGlobalEvent>

    override fun setUp() {
        super.setUp()
        cartGlobalEventObserver = mockk { every { onChanged(any()) } just Runs }
        cartViewModel.globalEvent.observeForever(cartGlobalEventObserver)
    }

    // region setAllAvailableItemCheck
    @Test
    fun `WHEN setAllAvailableItemCheck true to collapsed unselected group THEN should make all item checked`() {
        // GIVEN
        val itemCartOne = CartItemHolderData(isSelected = false)
        val itemCartTwo = CartItemHolderData(isSelected = false)

        val cartGroupHolderData = CartGroupHolderData(
            isAllSelected = false,
            isError = false,
            isCollapsed = true,
            productUiModelList = mutableListOf(
                itemCartOne,
                itemCartTwo
            )
        )
        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        cartViewModel.setAllAvailableItemCheck(true)

        // THEN
        verify {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
        val data = cartViewModel.cartDataList.value[0] as CartGroupHolderData
        assertEquals(true, data.isAllSelected)
        assertEquals(true, data.productUiModelList.all { it.isSelected })
    }

    @Test
    fun `WHEN setAllAvailableItemCheck true to collapsed selected group THEN should not change adapter data`() {
        // GIVEN
        val itemCartOne = CartItemHolderData(isSelected = false)
        val itemCartTwo = CartItemHolderData(isSelected = false)

        val cartGroupHolderData = CartGroupHolderData(
            isAllSelected = true,
            isError = false,
            isCollapsed = true,
            productUiModelList = mutableListOf(
                itemCartOne,
                itemCartTwo
            )
        )
        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        cartViewModel.setAllAvailableItemCheck(true)

        // THEN
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
    }

    @Test
    fun `WHEN setAllAvailableItemCheck true to expanded unselected group THEN should only make group item checked`() {
        // GIVEN
        val itemCartOne = CartItemHolderData(isSelected = true)
        val itemCartTwo = CartItemHolderData(isSelected = false)

        val cartGroupHolderData = CartGroupHolderData(
            isAllSelected = false,
            isError = false,
            isCollapsed = false,
            productUiModelList = mutableListOf(
                itemCartOne,
                itemCartTwo
            )
        )
        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        cartViewModel.setAllAvailableItemCheck(true)

        // THEN
        verify {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
        val data = cartViewModel.cartDataList.value[0] as CartGroupHolderData
        assertEquals(true, data.isAllSelected)
        assertEquals(true, data.productUiModelList[0].isSelected)
        assertEquals(false, data.productUiModelList[1].isSelected)
    }

    @Test
    fun `WHEN setAllAvailableItemCheck true to expanded selected group THEN should not change adapter data`() {
        // GIVEN
        val itemCartOne = CartItemHolderData(isSelected = true)
        val itemCartTwo = CartItemHolderData(isSelected = false)

        val cartGroupHolderData = CartGroupHolderData(
            isAllSelected = false,
            isError = false,
            isCollapsed = false,
            productUiModelList = mutableListOf(
                itemCartOne,
                itemCartTwo
            )
        )
        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        cartViewModel.setAllAvailableItemCheck(true)

        // THEN
        val data = cartViewModel.cartDataList.value[0] as CartGroupHolderData
        assertEquals(true, data.isAllSelected)
        assertEquals(true, data.productUiModelList[0].isSelected)
        assertEquals(false, data.productUiModelList[1].isSelected)
    }

    @Test
    fun `WHEN setAllAvailableItemCheck true to unselected cart item THEN should make item checked`() {
        // GIVEN
        val itemCartOne = CartItemHolderData(isSelected = false)
        cartViewModel.cartDataList.value = arrayListOf(itemCartOne)

        // WHEN
        cartViewModel.setAllAvailableItemCheck(true)

        // THEN
        verify {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
        val data = cartViewModel.cartDataList.value[0] as CartItemHolderData
        assertEquals(true, data.isSelected)
    }

    @Test
    fun `WHEN setAllAvailableItemCheck true to selected cart item THEN should not change adapter data`() {
        // GIVEN
        val itemCartOne = CartItemHolderData(isSelected = true)
        cartViewModel.cartDataList.value = arrayListOf(itemCartOne)

        // WHEN
        cartViewModel.setAllAvailableItemCheck(true)

        // THEN
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
    }

    @Test
    fun `WHEN setAllAvailableItemCheck false to selected cart item THEN should make item unchecked`() {
        // GIVEN
        val itemCartOne = CartItemHolderData(isSelected = true)
        cartViewModel.cartDataList.value = arrayListOf(itemCartOne)

        // WHEN
        cartViewModel.setAllAvailableItemCheck(false)

        // THEN
        verify {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
        val data = cartViewModel.cartDataList.value[0] as CartItemHolderData
        assertEquals(false, data.isSelected)
    }

    @Test
    fun `WHEN setAllAvailableItemCheck false to unselected cart item THEN should not change adapter data`() {
        // GIVEN
        val itemCartOne = CartItemHolderData(isSelected = false)
        cartViewModel.cartDataList.value = arrayListOf(itemCartOne)

        // WHEN
        cartViewModel.setAllAvailableItemCheck(false)

        // THEN
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
    }

    @Test
    fun `WHEN setAllAvailableItemCheck to error group item THEN should not change adapter data`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(isError = true)
        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        cartViewModel.setAllAvailableItemCheck(true)

        // THEN
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
    }

    @Test
    fun `WHEN setAllAvailableItemCheck to error cart item THEN should not change adapter data`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(isError = true)
        cartViewModel.cartDataList.value = arrayListOf(cartItemHolderData)

        // WHEN
        cartViewModel.setAllAvailableItemCheck(true)

        // THEN
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
    }

    @Test
    fun `WHEN setAllAvailableItemCheck to unavailable item THEN should not change adapter data`() {
        // GIVEN
        val disabledItem = DisabledItemHeaderHolderData()
        val cartSectionHeader = CartSectionHeaderHolderData()

        cartViewModel.cartDataList.value = arrayListOf(disabledItem, cartSectionHeader)

        // WHEN
        cartViewModel.setAllAvailableItemCheck(true)

        // THEN
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(1))
        }
    }
    // endregion

    // region setLastItemAlwaysSelected
    @Test
    fun `WHEN setLastItemAlwaysSelected group with one cart item THEN should return true`() {
        // GIVEN
        val itemCartOne = CartItemHolderData(isSelected = false)
        val itemCartTwo = CartItemHolderData(isSelected = false)

        val cartGroupHolderData = CartGroupHolderData(
            isAllSelected = false,
            isError = false,
            isCollapsed = true,
            productUiModelList = mutableListOf(
                itemCartOne
            )
        )
        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        val isSelected = cartViewModel.setLastItemAlwaysSelected()

        // THEN
        assertTrue(isSelected)
    }

    @Test
    fun `WHEN setLastItemAlwaysSelected group with multiple cart items THEN should return false`() {
        // GIVEN
        val itemCartOne = CartItemHolderData(isSelected = false)
        val itemCartTwo = CartItemHolderData(isSelected = false)

        val cartGroupHolderData = CartGroupHolderData(
            isAllSelected = false,
            isError = false,
            isCollapsed = true,
            productUiModelList = mutableListOf(
                itemCartOne,
                itemCartTwo
            )
        )
        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        val isSelected = cartViewModel.setLastItemAlwaysSelected()

        // THEN
        assertFalse(isSelected)
    }

    @Test
    fun `WHEN setLastItemAlwaysSelected with empty cart items THEN should return false`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(
            isAllSelected = false,
            isError = false,
            isCollapsed = true,
            productUiModelList = arrayListOf()
        )
        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        val isSelected = cartViewModel.setLastItemAlwaysSelected()

        // THEN
        assertFalse(isSelected)
    }
    // endregion

    // region removeAccordionDisabledItem
    @Test
    fun `WHEN removeAccordionDisabledItem THEN DisabledItemHeaderHolderData should be removed`() {
        // GIVEN
        val disabledItemHeaderHolderData = DisabledItemHeaderHolderData()
        cartViewModel.cartDataList.value = arrayListOf(disabledItemHeaderHolderData)

        // WHEN
        cartViewModel.removeAccordionDisabledItem()

        // THEN
        assertEquals(
            true,
            cartViewModel.cartDataList.value.any { it is DisabledItemHeaderHolderData }
        )
    }

    @Test
    fun `WHEN removeAccordionDisabledItem but DisabledItemHeaderHolderData not found THEN cartDataList should have same size`() {
        // GIVEN
        cartViewModel.cartDataList.value = arrayListOf(CartGroupHolderData(), CartItemHolderData())
        val previousSize = cartViewModel.cartDataList.value.size

        // WHEN
        cartViewModel.removeAccordionDisabledItem()

        // THEN
        assertEquals(previousSize, cartViewModel.cartDataList.value.size)
    }
    // endregion

    // region updateCartWishlistData
    @Test
    fun `WHEN updateCartWishlistData THEN wishlist in cartDataList should be updated`() {
        // GIVEN
        val previousWishlistHolderData = CartWishlistHolderData()
        val newCartWishlistHolderData = CartWishlistHolderData()
        cartViewModel.cartDataList.value = arrayListOf(previousWishlistHolderData)

        // WHEN
        cartViewModel.updateCartWishlistData(newCartWishlistHolderData)

        // THEN
        assertEquals(
            newCartWishlistHolderData,
            cartViewModel.cartDataList.value.find { it is CartWishlistHolderData }
        )
    }
    // endregion

    // region addCartWishlistData
    @Test
    fun `WHEN addCartWishlistData to cartDataList THEN cartDataList should be updated`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData()
        cartViewModel.cartDataList.value = arrayListOf(
            cartGroupHolderData,
            CartItemHolderData(),
            CartShopBottomHolderData(cartGroupHolderData),
            ShipmentSellerCashbackModel(),
            DisabledItemHeaderHolderData(),
            DisabledReasonHolderData(),
            DisabledAccordionHolderData()
        )
        var index = cartViewModel.cartDataList.value.lastIndex
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        val cartWishlistHolderData = CartWishlistHolderData()

        // WHEN
        cartViewModel.addCartWishlistData(cartSectionHeaderHolderData, cartWishlistHolderData)

        // THEN
        assertEquals(
            cartSectionHeaderHolderData,
            cartViewModel.cartDataList.value[++index]
        )
        assertEquals(
            cartWishlistHolderData,
            cartViewModel.cartDataList.value[++index]
        )
    }
    // endregion

    // region hasReachAllShopItems
    @Test
    fun `WHEN hasReachAllShopItems and data is below shop items THEN should return true`() {
        // GIVEN
        val recentViewData = CartRecentViewHolderData()
        val wishlistData = CartWishlistHolderData()
        val topAdsHeadlineData = CartTopAdsHeadlineData()
        val recommendationData = CartRecommendationItemHolderData(recommendationItem = RecommendationItem())

        // WHEN
        val recentViewResult = cartViewModel.hasReachAllShopItems(recentViewData)
        val cartWishlistResult = cartViewModel.hasReachAllShopItems(wishlistData)
        val cartTopAdsHeadlineResult = cartViewModel.hasReachAllShopItems(topAdsHeadlineData)
        val cartRecommendationResult = cartViewModel.hasReachAllShopItems(recommendationData)

        // THEN
        assertTrue(recentViewResult)
        assertTrue(cartWishlistResult)
        assertTrue(cartTopAdsHeadlineResult)
        assertTrue(cartRecommendationResult)
    }

    @Test
    fun `WHEN hasReachAllShopItems and data is in shop items THEN should return false`() {
        // GIVEN
        val groupData = CartGroupHolderData()
        val itemData = CartItemHolderData()

        // WHEN
        val groupResult = cartViewModel.hasReachAllShopItems(groupData)
        val itemResult = cartViewModel.hasReachAllShopItems(itemData)

        // THEN
        assertFalse(groupResult)
        assertFalse(itemResult)
    }
    // endregion

    // region getAllPromosApplied
    @Test
    fun `WHEN getAllPromosApplied with LastApplyPromoData THEN should return all promos`() {
        // GIVEN
        val lastApplyPromoData = LastApplyPromoData(
            codes = listOf("GLOBALCODE"),
            listVoucherOrders = listOf(
                VoucherOrders(code = "SAMPLEBO1"),
                VoucherOrders(code = "SAMPLEBO2")
            )
        )

        // WHEN
        val promoCodes = cartViewModel.getAllPromosApplied(lastApplyPromoData)

        // THEN
        assertEquals(3, promoCodes.size)
    }

    @Test
    fun `WHEN getAllPromosApplied with LastApplyUiModel THEN should return all promos`() {
        // GIVEN
        val lastApplyUiModel = LastApplyUiModel(
            codes = listOf("GLOBALCODE"),
            voucherOrders = listOf(
                LastApplyVoucherOrdersItemUiModel(code = "SAMPLEBO1"),
                LastApplyVoucherOrdersItemUiModel(code = "SAMPLEBO2")
            )
        )

        // WHEN
        val promoCodes = cartViewModel.getAllPromosApplied(lastApplyUiModel)

        // THEN
        assertEquals(3, promoCodes.size)
    }
    // endregion

    // region updateCartDataList
    @Test
    fun `WHEN updateCartDataList with empty cart items THEN should return false`() {
        // GIVEN
        val itemCartOne = CartItemHolderData(isSelected = false)
        val itemCartTwo = CartItemHolderData(isSelected = false)

        val cartGroupHolderData = CartGroupHolderData(
            isAllSelected = false,
            isError = false,
            isCollapsed = true,
            productUiModelList = mutableListOf(
                itemCartOne,
                itemCartTwo
            )
        )
        val newList = arrayListOf<Any>(cartGroupHolderData)

        // WHEN
        cartViewModel.updateCartDataList(newList)

        // THEN
        assertEquals(newList, cartViewModel.cartDataList.value)
    }
    // endregion

    override fun tearDown() {
        super.tearDown()
        cartViewModel.globalEvent.observeForever(cartGlobalEventObserver)
    }
}
