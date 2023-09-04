package com.tokopedia.cart.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cartrevamp.view.uimodel.CartCheckoutButtonState
import com.tokopedia.cartrevamp.view.uimodel.CartEmptyHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartGlobalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartLoadingHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSelectedAmountHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledCollapsedHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledReasonHolderData
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
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
    private lateinit var cartDataListObserver: Observer<ArrayList<Any>>
    private lateinit var cartCheckoutButtonStateObserver: Observer<CartCheckoutButtonState>

    override fun setUp() {
        super.setUp()
        cartGlobalEventObserver = mockk { every { onChanged(any()) } just Runs }
        cartDataListObserver = mockk { every { onChanged(any()) } just Runs }
        cartCheckoutButtonStateObserver = mockk { every { onChanged(any()) } just Runs }
        cartViewModel.globalEvent.observeForever(cartGlobalEventObserver)
        cartViewModel.cartCheckoutButtonState.observeForever(cartCheckoutButtonStateObserver)
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

    @Test
    fun `WHEN setLastItemAlwaysSelected with unavailable items THEN should return false`() {
        // GIVEN
        val disabledItemHeaderData = DisabledItemHeaderHolderData()
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        cartViewModel.cartDataList.value =
            arrayListOf(disabledItemHeaderData, cartSectionHeaderHolderData)

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
        cartViewModel.cartDataList.value =
            arrayListOf(CartGroupHolderData(), previousWishlistHolderData)

        // WHEN
        val wishlistIndex = cartViewModel.updateCartWishlistData(newCartWishlistHolderData)

        // THEN
        assertEquals(1, wishlistIndex)
        assertEquals(
            newCartWishlistHolderData,
            cartViewModel.cartDataList.value.find { it is CartWishlistHolderData }
        )
    }

    @Test
    fun `WHEN updateCartWishlistData but wishlist data not found THEN cartDataList should not be updated`() {
        // GIVEN
        val newCartWishlistHolderData = CartWishlistHolderData()
        cartViewModel.cartDataList.value = arrayListOf(CartGroupHolderData())

        // WHEN
        val wishlistIndex = cartViewModel.updateCartWishlistData(newCartWishlistHolderData)

        // THEN
        assertEquals(Int.ZERO, wishlistIndex)
    }

    @Test
    fun `WHEN updateCartWishlistData but cartDataList is empty THEN cartDataList should not be updated`() {
        // GIVEN
        val newCartWishlistHolderData = CartWishlistHolderData()
        cartViewModel.cartDataList.value = arrayListOf()

        // WHEN
        val wishlistIndex = cartViewModel.updateCartWishlistData(newCartWishlistHolderData)

        // THEN
        assertEquals(Int.ZERO, wishlistIndex)
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
        val recommendationData =
            CartRecommendationItemHolderData(recommendationItem = RecommendationItem())

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

    // region expand/collapse
    @Test
    fun `WHEN collapseDisabledItems THEN should notify cartDataList with collapsed item`() {
        // GIVEN
        val cartItemOne = CartItemHolderData()
        val cartItemTwo = CartItemHolderData()

        val topDataList = mutableListOf(
            CartGroupHolderData(),
            DisabledItemHeaderHolderData()
        )

        val shopList = mutableListOf(
            CartGroupHolderData(
                productUiModelList = mutableListOf(
                    cartItemOne,
                    cartItemTwo
                )
            ),
            cartItemOne,
            cartItemTwo,
        )

        val disabledAccordion = listOf(DisabledAccordionHolderData())

        val cartDataList = topDataList + shopList + disabledAccordion
        cartViewModel.cartDataList.value = ArrayList(cartDataList)
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.collapseDisabledItems()

        // THEN
        verify {
            cartDataListObserver.onChanged(any())
        }
        assertEquals(
            topDataList.size + disabledAccordion.size + 1,
            cartViewModel.cartDataList.value.size
        )
    }

    @Test
    fun `WHEN expandDisabledItems THEN should notify cartDataList with collapsed item`() {
        // GIVEN
        val cartItemOne = CartItemHolderData()
        val cartItemTwo = CartItemHolderData()
        val disabledCollapsedData = DisabledCollapsedHolderData(
            productUiModelList = mutableListOf(cartItemOne, cartItemTwo)
        )
        val allUnavailableShop = mutableListOf(
            CartGroupHolderData(
                productUiModelList = mutableListOf(
                    cartItemOne,
                    cartItemTwo
                )
            ),
            cartItemOne,
            cartItemTwo,
        )
        cartViewModel.cartModel.apply {
            tmpCollapsedUnavailableShop = disabledCollapsedData
            tmpAllUnavailableShop = allUnavailableShop
        }
        val cartDataList = arrayListOf(
            CartGroupHolderData(),
            DisabledItemHeaderHolderData(),
            disabledCollapsedData,
            DisabledAccordionHolderData()
        )
        cartViewModel.cartDataList.value = cartDataList
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.expandDisabledItems()

        // THEN
        verify {
            cartDataListObserver.onChanged(any())
        }
        assertEquals(
            cartDataList.size + allUnavailableShop.size,
            cartViewModel.cartDataList.value.size
        )
    }
    // endregion

    // region checkForShipmentForm
    @Test
    fun `WHEN checkFormShipmentForm and selectedAmount greater than zero THEN checkout button should be enabled`() {
        // GIVEN
        cartViewModel.selectedAmountState.value = 5

        // WHEN
        cartViewModel.checkForShipmentForm()

        // THEN
        verify {
            cartCheckoutButtonStateObserver.onChanged(CartCheckoutButtonState.ENABLE)
        }
    }

    @Test
    fun `WHEN checkFormShipmentForm and selectedAmount less or equal than zero THEN checkout button should be disabled`() {
        // GIVEN
        cartViewModel.selectedAmountState.value = 0

        // WHEN
        cartViewModel.checkForShipmentForm()

        // THEN
        verify {
            cartCheckoutButtonStateObserver.onChanged(CartCheckoutButtonState.DISABLE)
        }
    }
    // endregion

    // region addCartRecentViewData
    @Test
    fun `WHEN addCartRecentViewData to cartDataList THEN cartDataList should be updated`() {
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
        val cartRecentViewHolderData = CartRecentViewHolderData()

        // WHEN
        cartViewModel.addCartRecentViewData(cartSectionHeaderHolderData, cartRecentViewHolderData)

        // THEN
        assertEquals(
            cartSectionHeaderHolderData,
            cartViewModel.cartDataList.value[++index]
        )
        assertEquals(
            cartRecentViewHolderData,
            cartViewModel.cartDataList.value[++index]
        )
    }
    // endregion

    // region addCartRecommendationData
    @Test
    fun `WHEN addCartRecommendationData to cartDataList THEN cartDataList should be updated`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData()
        cartViewModel.cartDataList.value = arrayListOf(
            cartGroupHolderData,
            CartItemHolderData(),
            CartShopBottomHolderData(cartGroupHolderData),
            ShipmentSellerCashbackModel(),
            DisabledItemHeaderHolderData(),
            DisabledReasonHolderData(),
            DisabledAccordionHolderData(),
            CartRecentViewHolderData(),
            CartWishlistHolderData(),
            CartTopAdsHeadlineData(),
            CartRecommendationItemHolderData(recommendationItem = RecommendationItem())
        )
        var index = cartViewModel.cartDataList.value.lastIndex
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        val firstCartRecommendationItem =
            CartRecommendationItemHolderData(recommendationItem = RecommendationItem())
        val secondCartRecommendationItem =
            CartRecommendationItemHolderData(recommendationItem = RecommendationItem())
        val cartRecommendationItemHolderDataList = arrayListOf(
            firstCartRecommendationItem,
            secondCartRecommendationItem
        )

        // WHEN
        cartViewModel.addCartRecommendationData(
            cartSectionHeaderHolderData,
            cartRecommendationItemHolderDataList,
            1
        )

        // THEN
        assertEquals(
            cartSectionHeaderHolderData,
            cartViewModel.cartDataList.value[++index]
        )
        assertTrue(cartViewModel.cartDataList.value[++index] is CartTopAdsHeadlineData)
        assertEquals(
            firstCartRecommendationItem,
            cartViewModel.cartDataList.value[++index]
        )
        assertEquals(
            secondCartRecommendationItem,
            cartViewModel.cartDataList.value[++index]
        )
    }
    // endregion

    // region addCartTopAdsHeadlineData
    @Test
    fun `WHEN addCartTopAdsHeadlineData with index THEN cartDataList should be updated`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = arrayListOf(
                CartItemHolderData(),
                CartItemHolderData()
            )
        )
        cartViewModel.cartDataList.value = arrayListOf(
            cartGroupHolderData,
            CartItemHolderData(),
            CartShopBottomHolderData(cartGroupHolderData),
            ShipmentSellerCashbackModel(),
            DisabledItemHeaderHolderData(),
            DisabledReasonHolderData(),
            DisabledAccordionHolderData()
        )
        val lastIndex = cartViewModel.cartDataList.value.lastIndex

        // WHEN
        cartViewModel.addCartTopAdsHeadlineData(lastIndex + 1)

        // THEN
        assertTrue(cartViewModel.cartDataList.value[lastIndex + 1] is CartTopAdsHeadlineData)
    }

    @Test
    fun `WHEN addCartTopAdsHeadlineData with recommendation page THEN cartDataList should be updated`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = arrayListOf()
        )
        cartViewModel.cartDataList.value = arrayListOf(
            CartEmptyHolderData(),
            cartGroupHolderData,
            CartItemHolderData(),
            CartShopBottomHolderData(cartGroupHolderData),
            ShipmentSellerCashbackModel(),
            DisabledItemHeaderHolderData(),
            DisabledReasonHolderData(),
            DisabledAccordionHolderData(),
            CartRecentViewHolderData(),
            CartWishlistHolderData()
        )
        var lastIndex = cartViewModel.cartDataList.value.lastIndex
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        val recommendationPage = 1

        // WHEN
        cartViewModel.addCartTopAdsHeadlineData(cartSectionHeaderHolderData, recommendationPage)

        // THEN
        assertEquals(
            cartSectionHeaderHolderData,
            cartViewModel.cartDataList.value[++lastIndex]
        )
        assertTrue(cartViewModel.cartDataList.value[++lastIndex] is CartTopAdsHeadlineData)
    }

    @Test
    fun `WHEN addCartTopAdsHeadlineData with recommendation page not equal to one THEN cartDataList should not be updated`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = arrayListOf()
        )
        cartViewModel.cartDataList.value = arrayListOf(
            CartEmptyHolderData(),
            cartGroupHolderData,
            CartItemHolderData(),
            CartShopBottomHolderData(cartGroupHolderData),
            ShipmentSellerCashbackModel(),
            DisabledItemHeaderHolderData(),
            DisabledReasonHolderData(),
            DisabledAccordionHolderData(),
            CartWishlistHolderData()
        )
        val oldSize = cartViewModel.cartDataList.value.size
        val cartSectionHeaderHolderData = CartSectionHeaderHolderData()
        val recommendationPage = 2

        // WHEN
        cartViewModel.addCartTopAdsHeadlineData(cartSectionHeaderHolderData, recommendationPage)

        // THEN
        assertEquals(oldSize, cartViewModel.cartDataList.value.size)
    }
    // endregion

    // region resetData
    @Test
    fun `WHEN resetData THEN cartDataList should be cleared`() {
        // GIVEN
        cartViewModel.cartModel.firstCartSectionHeaderPosition = 0
        cartViewModel.cartDataList.value = arrayListOf(
            CartGroupHolderData(),
            CartItemHolderData(),
            CartItemHolderData()
        )
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.resetData()

        // THEN
        verify {
            cartCheckoutButtonStateObserver.onChanged(any())
            cartDataListObserver.onChanged(any())
        }
        assertEquals(
            arrayListOf<Any>(),
            cartViewModel.cartDataList.value
        )
    }
    // endregion

    // region cartLoading
    @Test
    fun `WHEN addCartLoadingData but cartLoadingHolderData is null THEN cartDataList should generate new CartLoadingHolderData`() {
        // GIVEN
        cartViewModel.cartModel.cartLoadingHolderData = null
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.addCartLoadingData()

        // THEN
        verify { cartDataListObserver.onChanged(any()) }
        assertTrue(cartViewModel.cartModel.cartLoadingHolderData is CartLoadingHolderData)
    }

    @Test
    fun `WHEN addCartLoadingData THEN cartDataList should has CartLoadingHolderData`() {
        // GIVEN
        cartViewModel.cartModel.cartLoadingHolderData = CartLoadingHolderData()
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.addCartLoadingData()

        // THEN
        verify { cartDataListObserver.onChanged(any()) }
        assertEquals(
            cartViewModel.cartModel.cartLoadingHolderData,
            cartViewModel.cartDataList.value[0]
        )
    }

    @Test
    fun `WHEN removeCartLoadingData but CartLoadingHolderData null THEN cartDataList should not updated`() {
        // GIVEN
        cartViewModel.cartModel.cartLoadingHolderData = null
        cartViewModel.cartDataList.value =
            arrayListOf(CartGroupHolderData(), CartLoadingHolderData())

        // WHEN
        cartViewModel.removeCartLoadingData()

        // THEN
        assertEquals(2, cartViewModel.cartDataList.value.size)
    }

    @Test
    fun `WHEN removeCartLoadingData but CartLoadingHolderData is different with cartDataList loading THEN cartDataList should not updated`() {
        // GIVEN
        cartViewModel.cartModel.cartLoadingHolderData = CartLoadingHolderData()
        cartViewModel.cartDataList.value =
            arrayListOf(CartGroupHolderData(), CartLoadingHolderData())

        // WHEN
        cartViewModel.removeCartLoadingData()

        // THEN
        assertEquals(2, cartViewModel.cartDataList.value.size)
    }

    @Test
    fun `WHEN removeCartLoadingData but CartLoadingHolderData is not in cartDataList THEN cartDataList should not updated`() {
        // GIVEN
        cartViewModel.cartModel.cartLoadingHolderData = CartLoadingHolderData()
        cartViewModel.cartDataList.value = arrayListOf(CartGroupHolderData())

        // WHEN
        cartViewModel.removeCartLoadingData()

        // THEN
        assertEquals(1, cartViewModel.cartDataList.value.size)
    }

    @Test
    fun `WHEN removeCartLoadingData THEN cartDataList should remove CartLoadingHolderData`() {
        // GIVEN
        val loadingData = CartLoadingHolderData()
        cartViewModel.cartModel.cartLoadingHolderData = loadingData
        cartViewModel.cartDataList.value = arrayListOf(loadingData)
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.removeCartLoadingData()

        // THEN
        verify { cartDataListObserver.onChanged(any()) }
        assertEquals(0, cartViewModel.cartDataList.value.size)
    }
    // endregion

    // region addItems
    @Test
    fun `WHEN addItems to cartDataList THEN cartDataList need to be updated`() {
        // GIVEN
        val currentList = arrayListOf(CartGroupHolderData(), CartItemHolderData())
        val currentSize = currentList.size
        cartViewModel.cartDataList.value = currentList
        val toBeInsertedList = listOf(
            CartGroupHolderData(),
            CartItemHolderData(),
            CartItemHolderData()
        )

        // WHEN
        cartViewModel.addItems(toBeInsertedList)

        // THEN
        assertEquals(
            currentSize + toBeInsertedList.size,
            cartViewModel.cartDataList.value.size
        )
    }

    @Test
    fun `WHEN addItems with index to cartDataList THEN cartDataList need to be updated`() {
        // GIVEN
        val currentList = arrayListOf(
            CartGroupHolderData(
                cartString = "1"
            ), CartItemHolderData()
        )
        cartViewModel.cartDataList.value = currentList
        val toBeInsertedList = listOf(
            CartGroupHolderData(cartString = "2"),
            CartItemHolderData(),
            CartItemHolderData()
        )

        // WHEN
        cartViewModel.addItems(0, toBeInsertedList)

        // THEN
        val firstGroupHolderData =
            cartViewModel.cartDataList.value.find { it is CartGroupHolderData } as CartGroupHolderData
        assertEquals(
            (toBeInsertedList[0] as CartGroupHolderData).cartString,
            firstGroupHolderData.cartString
        )
    }
    // endregion

    // region setShopSelected
    @Test
    fun `WHEN setShopSelected to true THEN all cartDataList items isSelected must be true`() {
        // GIVEN
        val productList = mutableListOf(
            CartItemHolderData(isSelected = true),
            CartItemHolderData(isSelected = false)
        )
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = productList
        )
        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        cartViewModel.setShopSelected(0, true)

        // THEN
        assertTrue(cartGroupHolderData.isAllSelected)
        assertTrue(cartGroupHolderData.productUiModelList.all { it.isSelected })
    }

    @Test
    fun `WHEN setShopSelected to false THEN all cartDataList items isSelected must be false`() {
        // GIVEN
        val productList = mutableListOf(
            CartItemHolderData(isSelected = true),
            CartItemHolderData(isSelected = false)
        )
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = productList
        )
        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        cartViewModel.setShopSelected(0, false)

        // THEN
        assertFalse(cartGroupHolderData.isAllSelected)
        assertTrue(cartGroupHolderData.productUiModelList.none { it.isSelected })
    }

    @Test
    fun `WHEN setShopSelected to true but position is not group item THEN should not update anything`() {
        // GIVEN
        val productList = mutableListOf(
            CartItemHolderData(isSelected = false),
            CartItemHolderData(isSelected = true)
        )
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = productList
        )
        cartViewModel.cartDataList.value = arrayListOf(
            CartSelectedAmountHolderData(),
            cartGroupHolderData
        )

        // WHEN
        cartViewModel.setShopSelected(0, true)

        // THEN
        assertFalse(cartGroupHolderData.isAllSelected)
        assertFalse(cartGroupHolderData.productUiModelList[0].isSelected)
        assertTrue(cartGroupHolderData.productUiModelList[1].isSelected)
    }

    @Test
    fun `WHEN setShopSelected to false but position is not group item THEN should not update anything`() {
        // GIVEN
        val productList = mutableListOf(
            CartItemHolderData(isSelected = false),
            CartItemHolderData(isSelected = true)
        )
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = productList
        )
        cartViewModel.cartDataList.value = arrayListOf(
            CartSelectedAmountHolderData(),
            cartGroupHolderData
        )

        // WHEN
        cartViewModel.setShopSelected(0, false)

        // THEN
        assertFalse(cartGroupHolderData.isAllSelected)
        assertFalse(cartGroupHolderData.productUiModelList[0].isSelected)
        assertTrue(cartGroupHolderData.productUiModelList[1].isSelected)
    }
    // endregion

    // region doClearAllPromo
    @Test
    fun `WHEN doClearAllPromo THEN call clearCacheAutoApply use case`() {
        // GIVEN
        cartViewModel.cartModel.lastValidateUseRequest = ValidateUsePromoRequest(
            orders = listOf(
                OrdersItem(codes = mutableListOf("BO1", "MVC1")),
                OrdersItem(codes = mutableListOf("BO2", "MVC2", "MVC3"))
            )
        )
        coEvery {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel()

        // WHEN
        cartViewModel.doClearAllPromo()

        // THEN
        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
        assertEquals(
            false,
            cartViewModel.cartModel.isLastApplyResponseStillValid
        )
        assertTrue(cartViewModel.cartModel.lastValidateUseResponse?.code?.isEmpty() == true)
    }
    // endregion

    // region updateRecentViewData
    @Test
    fun `WHEN updateRecentViewData THEN recent view data in cartDataList should be updated`() {
        // GIVEN
        cartViewModel.cartDataList.value = arrayListOf(
            CartGroupHolderData(),
            CartItemHolderData(),
            CartItemHolderData(),
            CartRecentViewHolderData(
                recentViewList = listOf(
                    CartRecentViewItemHolderData(id = "123")
                )
            )
        )
        cartViewModel.cartDataList.observeForever(cartDataListObserver)
        val productId = "123"
        val isWishlist = true

        // WHEN
        cartViewModel.updateRecentViewData(productId, isWishlist)

        // THEN
        verify {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
    }
    // endregion

    // region removeWishlist
    @Test
    fun `WHEN removeWishlist from cartDataList THEN global event need to change to OnNeedUpdateWishlistAdapterData`() {
        // GIVEN
        val productId = "123"
        cartViewModel.cartDataList.value = arrayListOf(
            CartGroupHolderData(),
            CartItemHolderData(),
            CartItemHolderData(),
            CartWishlistHolderData(
                wishList = mutableListOf(
                    CartWishlistItemHolderData(id = "123"),
                    CartWishlistItemHolderData(id = "124")
                )
            )
        )

        // WHEN
        cartViewModel.removeWishlist(productId)

        // THEN
        assertTrue(cartViewModel.globalEvent.value is CartGlobalEvent.OnNeedUpdateWishlistAdapterData)
    }
    // endregion

    override fun tearDown() {
        super.tearDown()
        cartViewModel.globalEvent.removeObserver(cartGlobalEventObserver)
        cartViewModel.cartDataList.removeObserver(cartDataListObserver)
        cartViewModel.cartCheckoutButtonState.removeObserver(cartCheckoutButtonStateObserver)
    }
}
