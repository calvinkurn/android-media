package com.tokopedia.cart.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cartrevamp.view.uimodel.AddCartToWishlistV2Event
import com.tokopedia.cartrevamp.view.uimodel.CartAddOnData
import com.tokopedia.cartrevamp.view.uimodel.CartAddOnProductData
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
import com.tokopedia.cartrevamp.view.uimodel.RemoveFromWishlistEvent
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
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
            cartItemTwo
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
        val allUnavailableShop = mutableListOf<Any>(
            CartGroupHolderData(
                productUiModelList = mutableListOf(
                    cartItemOne,
                    cartItemTwo
                )
            ),
            cartItemOne,
            cartItemTwo
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
        cartViewModel.selectedAmountState.value = Pair(0, 1)

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
        cartViewModel.selectedAmountState.value = Pair(0, 0)

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
        val currentList = arrayListOf<Any>(CartGroupHolderData(), CartItemHolderData())
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
        val currentList = arrayListOf<Any>(
            CartGroupHolderData(
                cartString = "1"
            ),
            CartItemHolderData()
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

    @Test
    fun `WHEN updateRecentViewData with empty recentViewList THEN recent view data in cartDataList should not be updated`() {
        // GIVEN
        cartViewModel.cartDataList.value = arrayListOf(
            CartGroupHolderData(),
            CartItemHolderData(),
            CartItemHolderData(),
            CartRecentViewHolderData(recentViewList = emptyList())
        )
        cartViewModel.cartDataList.observeForever(cartDataListObserver)
        val productId = "123"
        val isWishlist = true

        // WHEN
        cartViewModel.updateRecentViewData(productId, isWishlist)

        // THEN
        verify(inverse = true) {
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

    @Test
    fun `WHEN removeWishlist from cartDataList with empty wishlist THEN should notify all data`() {
        // GIVEN
        val productId = "123"
        val cartWishlistHolderData = CartWishlistHolderData(wishList = mutableListOf())
        cartViewModel.cartDataList.value = arrayListOf(
            CartGroupHolderData(),
            CartItemHolderData(),
            CartItemHolderData(),
            cartWishlistHolderData
        )
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.removeWishlist(productId)

        // THEN
        assertFalse(cartViewModel.globalEvent.value is CartGlobalEvent.OnNeedUpdateWishlistAdapterData)
    }
    // endregion

    // region updateWishlistHolderData
    @Test
    fun `WHEN updateWishlistHolderData to wishlist THEN data in cartDataList should be updated`() {
        // GIVEN
        val cartWishlistItemHolderData = CartWishlistItemHolderData(id = "123")
        val cartWishlistItemHolderDataTwo = CartWishlistItemHolderData(id = "124")
        val cartWishlistHolderData = CartWishlistHolderData(
            wishList = mutableListOf(
                cartWishlistItemHolderData,
                cartWishlistItemHolderDataTwo
            )
        )
        val productId = "123"
        cartViewModel.cartDataList.value = arrayListOf(
            CartGroupHolderData(),
            CartItemHolderData(),
            CartItemHolderData(),
            cartWishlistHolderData
        )
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.updateWishlistHolderData(productId, true)

        // THEN
        assertEquals(true, cartWishlistItemHolderData.isWishlist)
        verify {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
    }

    @Test
    fun `WHEN updateWishlistHolderData with empty wishlist THEN data in cartDataList should not be updated`() {
        // GIVEN
        val cartWishlistHolderData = CartWishlistHolderData(wishList = mutableListOf())
        val productId = "123"
        cartViewModel.cartDataList.value = arrayListOf(
            CartGroupHolderData(),
            CartItemHolderData(),
            CartItemHolderData(),
            cartWishlistHolderData
        )
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.updateWishlistHolderData(productId, false)

        // THEN
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
    }

    @Test
    fun `WHEN updateWishlistHolderData to non wishlist THEN data in cartDataList should be updated`() {
        // GIVEN
        val cartWishlistItemHolderData = CartWishlistItemHolderData(id = "123")
        val cartWishlistItemHolderDataTwo = CartWishlistItemHolderData(id = "124")
        val cartWishlistHolderData = CartWishlistHolderData(
            wishList = mutableListOf(
                cartWishlistItemHolderData,
                cartWishlistItemHolderDataTwo
            )
        )
        val productId = "123"
        cartViewModel.cartDataList.value = arrayListOf(
            CartGroupHolderData(),
            CartItemHolderData(),
            CartItemHolderData(),
            cartWishlistHolderData
        )
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.updateWishlistHolderData(productId, false)

        // THEN
        assertEquals(false, cartWishlistItemHolderData.isWishlist)
        verify {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
    }

    @Test
    fun `WHEN updateWishlistHolderData with no wishlist data THEN data in cartDataList should be updated`() {
        // GIVEN
        val productId = "123"
        cartViewModel.cartDataList.value = arrayListOf(
            CartGroupHolderData(),
            CartItemHolderData(),
            CartItemHolderData()
        )
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.updateWishlistHolderData(productId, false)

        // THEN
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
    }
    // end region

    // region updateWishlistDataByProductId
    @Test
    fun `WHEN updateWishlistDataByProductId to wishlist with collapsed state THEN should udpate group item`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(productId = "123")
        val cartItemHolderDataTwo = CartItemHolderData(productId = "124")
        val cartGroupHolderData = CartGroupHolderData(
            isCollapsed = true,
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo
            )
        )
        val productId = "123"
        cartViewModel.cartDataList.value = arrayListOf(
            cartGroupHolderData,
            cartItemHolderData,
            cartItemHolderDataTwo
        )
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.updateWishlistDataByProductId(productId, true)

        // THEN
        assertEquals(true, cartGroupHolderData.productUiModelList[0].isWishlisted)
        assertEquals(true, cartItemHolderData.isWishlisted)

        assertEquals(false, cartGroupHolderData.productUiModelList[1].isWishlisted)
        assertEquals(false, cartItemHolderDataTwo.isWishlisted)

        verify {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
        }
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(1))
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(2))
        }
    }

    @Test
    fun `WHEN updateWishlistDataByProductId to wishlist with expanded group THEN should update cart item`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(productId = "123")
        val cartItemHolderDataTwo = CartItemHolderData(productId = "124")
        val cartGroupHolderData = CartGroupHolderData(
            isCollapsed = false,
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo
            )
        )
        val productId = "123"
        cartViewModel.cartDataList.value = arrayListOf(
            cartGroupHolderData,
            cartItemHolderData,
            cartItemHolderDataTwo
        )
        cartViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        cartViewModel.updateWishlistDataByProductId(productId, true)

        // THEN
        assertEquals(true, cartGroupHolderData.productUiModelList[0].isWishlisted)
        assertEquals(true, cartItemHolderData.isWishlisted)

        assertEquals(false, cartGroupHolderData.productUiModelList[1].isWishlisted)
        assertEquals(false, cartItemHolderDataTwo.isWishlisted)

        verify {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(1))
        }
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(2))
        }
    }
    // endregion

    // region updateAddOnByCartId
    @Test
    fun `WHEN updateAddOnByCartId THEN data in cartDataList should be updated`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartId = "123",
            addOnsProduct = CartAddOnData(
                listData = arrayListOf(
                    CartAddOnProductData()
                )
            )
        )
        val cartItemHolderDataTwo = CartItemHolderData(cartId = "124")
        val newAddOnWording = "new add on wording"
        val addonsOne = AddOnUIModel()
        val addonsTwo = AddOnUIModel()
        val selectedAddons = arrayListOf(addonsOne, addonsTwo)
        val cartId = "123"
        val spyViewModel = spyk(cartViewModel)
        spyViewModel.cartDataList.value = arrayListOf(
            CartGroupHolderData(),
            cartItemHolderData,
            cartItemHolderDataTwo
        )
        spyViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        spyViewModel.updateAddOnByCartId(cartId, newAddOnWording, selectedAddons)

        // THEN
        assertEquals(selectedAddons.size, cartItemHolderData.addOnsProduct.listData.size)
        verifyOrder {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(1))
            spyViewModel.reCalculateSubTotal()
            cartGlobalEventObserver.onChanged(CartGlobalEvent.SubTotalUpdated(0.0, "0", 0.0, false))
        }
    }

    @Test
    fun `WHEN updateAddOnByCartId with no matching cart id THEN data in cartDataList should be updated`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartId = "124",
            addOnsProduct = CartAddOnData(
                listData = arrayListOf(
                    CartAddOnProductData()
                )
            )
        )
        val cartItemHolderDataTwo = CartItemHolderData(cartId = "125")
        val newAddOnWording = "new add on wording"
        val addonsOne = AddOnUIModel()
        val addonsTwo = AddOnUIModel()
        val selectedAddons = arrayListOf(addonsOne, addonsTwo)
        val cartId = "123"
        val spyViewModel = spyk(cartViewModel)
        spyViewModel.cartDataList.value = arrayListOf(
            CartGroupHolderData(),
            cartItemHolderData,
            cartItemHolderDataTwo
        )
        spyViewModel.cartDataList.observeForever(cartDataListObserver)

        // WHEN
        spyViewModel.updateAddOnByCartId(cartId, newAddOnWording, selectedAddons)

        // THEN
        assertEquals(1, cartItemHolderData.addOnsProduct.listData.size)
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(0))
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(1))
            cartGlobalEventObserver.onChanged(CartGlobalEvent.AdapterItemChanged(2))
        }
        verify {
            spyViewModel.reCalculateSubTotal()
        }
    }
    // endregion

    // region emitTokonowUpdated
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN emitTokonowUpdated true THEN should emit true`() {
        runTest(UnconfinedTestDispatcher()) {
            val deferred = async {
                cartViewModel.tokoNowProductUpdater.first()
            }

            cartViewModel.emitTokonowUpdated(true)

            assertTrue(deferred.await())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN emitTokonowUpdated false THEN should emit false`() {
        runTest(UnconfinedTestDispatcher()) {
            val deferred = async {
                cartViewModel.tokoNowProductUpdater.first()
            }

            cartViewModel.emitTokonowUpdated(false)

            assertFalse(deferred.await())
        }
    }
    // endregion

    // region updateShowShownByCartGroup
    @Test
    fun `WHEN updateShopShownByCartGroup THEN first shop in each group data should set to true`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartId = "124",
            cartStringOrder = "222"
        )
        val cartItemHolderDataTwo = CartItemHolderData(
            cartId = "125",
            isShopShown = true,
            cartStringOrder = "222"
        )

        val cartGroupHolderData = CartGroupHolderData(
            uiGroupType = 1,
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo
            )
        )

        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        cartViewModel.updateShopShownByCartGroup(cartGroupHolderData)

        // THEN
        assertTrue(cartItemHolderData.isShopShown)
        assertFalse(cartItemHolderDataTwo.isShopShown)
    }

    @Test
    fun `WHEN updateShopShownByCartGroup with non owoc design THEN shop shown should false`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartId = "124",
            cartStringOrder = "222"
        )
        val cartItemHolderDataTwo = CartItemHolderData(
            cartId = "125",
            cartStringOrder = "222"
        )
        val cartGroupHolderData = CartGroupHolderData(
            uiGroupType = 0,
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo
            )
        )

        cartViewModel.cartDataList.value = arrayListOf(cartGroupHolderData)

        // WHEN
        cartViewModel.updateShopShownByCartGroup(cartGroupHolderData)

        // THEN
        assertFalse(cartItemHolderData.isShopShown)
        assertFalse(cartItemHolderDataTwo.isShopShown)
    }
    // endregion

    // region processAddCartToWishlist
    @Test
    fun `WHEN processAddCartToWishlist success THEN should notify success`() {
        // GIVEN
        val productId = "123"
        val userId = "12223"
        val isLastItem = true
        val source = "source"
        every { addToWishListV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishListV2UseCase.executeOnBackground() } returns Success(
            AddToWishlistV2Response.Data.WishlistAddV2()
        )

        // WHEN
        cartViewModel.processAddCartToWishlist(
            productId,
            userId,
            isLastItem,
            source
        )

        // THEN
        assertTrue(cartViewModel.addCartToWishlistV2Event.value is AddCartToWishlistV2Event.Success)
    }

    @Test
    fun `WHEN processAddCartToWishlist failed THEN should notify failed`() {
        // GIVEN
        val productId = "123"
        val userId = "12223"
        val isLastItem = true
        val source = "source"
        val exception = mockk<Throwable>("fail")
        every { addToWishListV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishListV2UseCase.executeOnBackground() } returns Fail(exception)

        // WHEN
        cartViewModel.processAddCartToWishlist(
            productId,
            userId,
            isLastItem,
            source
        )

        // THEN
        assertTrue(cartViewModel.addCartToWishlistV2Event.value is AddCartToWishlistV2Event.Failed)
    }
    // endregion

    // region processRemoveFromWishlistV2
    @Test
    fun `WHEN processRemoveFromWishlistV2 from cart success THEN should notify success from cart`() {
        // GIVEN
        val productId = "123"
        val userId = "12223"
        val isFromCart = true
        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(
            DeleteWishlistV2Response.Data.WishlistRemoveV2()
        )

        // WHEN
        cartViewModel.processRemoveFromWishlistV2(
            productId,
            userId,
            isFromCart
        )

        // THEN
        assertTrue(cartViewModel.removeFromWishlistEvent.value is RemoveFromWishlistEvent.RemoveWishlistFromCartSuccess)
    }

    @Test
    fun `WHEN processRemoveFromWishlistV2 not from cart THEN should notify success`() {
        // GIVEN
        val productId = "123"
        val userId = "12223"
        val isFromCart = false
        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(
            DeleteWishlistV2Response.Data.WishlistRemoveV2()
        )

        // WHEN
        cartViewModel.processRemoveFromWishlistV2(
            productId,
            userId,
            isFromCart
        )

        // THEN
        assertTrue(cartViewModel.removeFromWishlistEvent.value is RemoveFromWishlistEvent.Success)
    }

    @Test
    fun `WHEN processRemoveFromWishlistV2 from cart failed THEN should notify failed from cart`() {
        // GIVEN
        val productId = "123"
        val userId = "12223"
        val isFromCart = true
        val exception = mockk<Throwable>("fail")
        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(exception)

        // WHEN
        cartViewModel.processRemoveFromWishlistV2(
            productId,
            userId,
            isFromCart
        )

        // THEN
        assertTrue(cartViewModel.removeFromWishlistEvent.value is RemoveFromWishlistEvent.RemoveWishlistFromCartFailed)
    }

    @Test
    fun `WHEN processRemoveFromWishlistV2 not from cart failed THEN should notify failed`() {
        // GIVEN
        val productId = "123"
        val userId = "12223"
        val isFromCart = false
        val exception = mockk<Throwable>("fail")
        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(exception)

        // WHEN
        cartViewModel.processRemoveFromWishlistV2(
            productId,
            userId,
            isFromCart
        )

        // THEN
        assertTrue(cartViewModel.removeFromWishlistEvent.value is RemoveFromWishlistEvent.Failed)
    }
    // end region

    // region removeProductByCartId
    @Test
    fun `WHEN removeProductByCartId THEN should return correct new list after being deleted`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(cartId = "123", isSelected = true)
        val cartItemHolderDataTwo = CartItemHolderData(cartId = "124", isSelected = false)
        val cartItemHolderDataThree = CartItemHolderData(cartId = "125", isSelected = true)
        val cartItemHolderDataFour = CartItemHolderData(cartId = "126", isSelected = false)
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo,
                cartItemHolderDataThree,
                cartItemHolderDataFour
            )
        )

        val disabledReasonHolderData = DisabledReasonHolderData()
        val cartItemHolderSecondData =
            CartItemHolderData(cartId = "234", isSelected = true, isError = true)
        val cartItemHolderSecondDataTwo =
            CartItemHolderData(cartId = "235", isSelected = true, isError = true)
        val cartGroupHolderDataTwo = CartGroupHolderData(
            isError = true,
            productUiModelList = mutableListOf(
                cartItemHolderSecondData,
                cartItemHolderSecondDataTwo
            )
        )

        val cartItemHolderThirdData = CartItemHolderData(cartId = "345")
        val cartGroupHolderDataThree = CartGroupHolderData(
            productUiModelList = mutableListOf(cartItemHolderThirdData)
        )

        val cartItemHolderFourData = CartItemHolderData(cartId = "456")
        val cartGroupHolderDataFour = CartGroupHolderData(
            productUiModelList = mutableListOf(cartItemHolderFourData)
        )

        val selectedAmountHolderData = CartSelectedAmountHolderData()
        val disabledItemHeaderHolderData = DisabledItemHeaderHolderData()
        val disabledAccordionHolderData = DisabledAccordionHolderData()

        cartViewModel.cartDataList.value = arrayListOf(
            selectedAmountHolderData,
            cartGroupHolderData,
            cartItemHolderData,
            cartItemHolderDataTwo,
            cartItemHolderDataThree,
            cartItemHolderDataFour,
            cartGroupHolderDataThree,
            cartItemHolderThirdData,
            cartGroupHolderDataFour,
            cartItemHolderFourData,
            disabledItemHeaderHolderData,
            disabledReasonHolderData,
            cartGroupHolderDataTwo,
            cartItemHolderSecondData,
            cartItemHolderSecondDataTwo,
            disabledAccordionHolderData,
            CartWishlistHolderData(),
            CartRecommendationItemHolderData(recommendationItem = RecommendationItem()),
            CartTopAdsHeadlineData(),
            CartRecentViewHolderData()
        )

        // WHEN
        val newCartDataList = cartViewModel.removeProductByCartId(
            listOf("123", "124", "234", "235", "456"),
            needRefresh = true,
            isFromGlobalCheckbox = false
        )

        // THEN
        assertEquals(10, newCartDataList.size)
    }

    @Test
    fun `WHEN removeProductByCartId all availableItems THEN cartSelectedAmountHolder should be deleted`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(cartId = "123", isSelected = true)
        val cartItemHolderDataTwo = CartItemHolderData(cartId = "124", isSelected = false)
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo
            )
        )

        val selectedAmountHolderData = CartSelectedAmountHolderData()

        cartViewModel.cartDataList.value = arrayListOf(
            selectedAmountHolderData,
            cartGroupHolderData,
            cartItemHolderData,
            cartItemHolderDataTwo
        )

        // WHEN
        val newCartDataList = cartViewModel.removeProductByCartId(
            listOf("123", "124"),
            needRefresh = true,
            isFromGlobalCheckbox = false
        )

        // THEN
        assertTrue(newCartDataList.isEmpty())
    }

    @Test
    fun `WHEN removeProductByCartId some of disabled items THEN should remove accordion if disabled items less than three items`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(cartId = "123", isSelected = true)
        val cartItemHolderDataTwo = CartItemHolderData(cartId = "124", isSelected = false)
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo
            )
        )

        val disabledReasonHolderData = DisabledReasonHolderData()
        val disabledCartItemHolderData =
            CartItemHolderData(cartId = "234", isSelected = true, isError = true)
        val disabledCartItemHolderSecondData =
            CartItemHolderData(cartId = "235", isSelected = true, isError = true)
        val disabledCartGroupHolderData = CartGroupHolderData(
            isError = true,
            productUiModelList = mutableListOf(
                disabledCartItemHolderData,
                disabledCartItemHolderSecondData
            )
        )

        val selectedAmountHolderData = CartSelectedAmountHolderData()
        val disabledItemHeaderHolderData = DisabledItemHeaderHolderData()
        val disabledAccordionHolderData = DisabledAccordionHolderData()

        cartViewModel.cartDataList.value = arrayListOf(
            selectedAmountHolderData,
            cartGroupHolderData,
            cartItemHolderData,
            cartItemHolderDataTwo,
            disabledItemHeaderHolderData,
            disabledReasonHolderData,
            disabledCartGroupHolderData,
            disabledCartItemHolderData,
            disabledCartItemHolderSecondData,
            disabledAccordionHolderData
        )

        // WHEN
        val newCartDataList = cartViewModel.removeProductByCartId(
            listOf("234"),
            needRefresh = true,
            isFromGlobalCheckbox = false
        )

        // THEN
        assertEquals(8, newCartDataList.size)
    }

    @Test
    fun `WHEN checkAvailableShopBottomHolderData THEN should remove cartShopBottomHolderData if product is empty after deletion`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = mutableListOf()
        )
        val cartShopBottomHolderData = CartShopBottomHolderData(cartGroupHolderData)

        val newCartDataList = arrayListOf(
            cartGroupHolderData,
            cartShopBottomHolderData
        )
        val toBeRemovedItems: MutableList<Any> = mutableListOf()

        // WHEN
        cartViewModel.checkAvailableShopBottomHolderData(
            newCartDataList,
            0,
            cartGroupHolderData,
            toBeRemovedItems
        )

        // THEN
        assertEquals(1, toBeRemovedItems.size)
        assertEquals(cartShopBottomHolderData, toBeRemovedItems[0])
    }

    @Test
    fun `WHEN checkAvailableShopBottomHolderData for tokonow THEN should expand if after deletion is less than or equal 3`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(cartId = "123", isSelected = true)
        val cartItemHolderDataTwo = CartItemHolderData(cartId = "124", isSelected = false)
        val cartItemHolderDataThree = CartItemHolderData(cartId = "125", isSelected = true)
        val cartGroupHolderData = CartGroupHolderData(
            isTokoNow = true,
            isCollapsed = true,
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo,
                cartItemHolderDataThree
            )
        )
        val cartShopBottomHolderData = CartShopBottomHolderData(cartGroupHolderData)

        val newCartDataList = arrayListOf(
            cartGroupHolderData,
            cartShopBottomHolderData
        )
        val toBeRemovedItems: MutableList<Any> = mutableListOf()

        // WHEN
        cartViewModel.checkAvailableShopBottomHolderData(
            newCartDataList,
            0,
            cartGroupHolderData,
            toBeRemovedItems
        )

        // THEN
        val newCartGroupHolderData = newCartDataList[0] as CartGroupHolderData
        val newCartShopBottomHolderData = newCartDataList[4] as CartShopBottomHolderData
        assertTrue(toBeRemovedItems.isEmpty())
        assertEquals(5, newCartDataList.size)
        assertEquals(newCartGroupHolderData, newCartShopBottomHolderData.shopData)
        assertFalse(newCartGroupHolderData.isCollapsible)
        assertFalse(newCartGroupHolderData.isCollapsed)
    }

    @Test
    fun `WHEN checkAvailableShopBottomHolderData for tokonow THEN should update data only if after deletion is more than 3`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(cartId = "123", isSelected = true)
        val cartItemHolderDataTwo = CartItemHolderData(cartId = "124", isSelected = false)
        val cartItemHolderDataThree = CartItemHolderData(cartId = "125", isSelected = true)
        val cartItemHolderDataFour = CartItemHolderData(cartId = "126", isSelected = true)
        val cartGroupHolderData = CartGroupHolderData(
            isTokoNow = true,
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo,
                cartItemHolderDataThree,
                cartItemHolderDataFour
            )
        )
        val cartShopBottomHolderData = CartShopBottomHolderData(cartGroupHolderData)

        val newCartDataList = arrayListOf(
            cartGroupHolderData,
            cartItemHolderData,
            cartItemHolderDataTwo,
            cartItemHolderDataThree,
            cartItemHolderDataFour,
            cartShopBottomHolderData
        )
        val toBeRemovedItems: MutableList<Any> = mutableListOf()

        // WHEN
        cartViewModel.checkAvailableShopBottomHolderData(
            newCartDataList,
            0,
            cartGroupHolderData,
            toBeRemovedItems
        )

        // THEN
        val newCartGroupHolderData = newCartDataList[0] as CartGroupHolderData
        val newCartShopBottomHolderData = newCartDataList[5] as CartShopBottomHolderData
        assertTrue(toBeRemovedItems.isEmpty())
        assertEquals(6, newCartDataList.size)
        assertEquals(newCartGroupHolderData, newCartShopBottomHolderData.shopData)
        assertTrue(newCartGroupHolderData.isCollapsible)
    }

    @Test
    fun `WHEN checkAvailableShopBottomHolderData but no shopBottomHolder THEN should return same data`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(cartId = "123", isSelected = true)
        val cartItemHolderDataTwo = CartItemHolderData(cartId = "124", isSelected = false)
        val cartGroupHolderData = CartGroupHolderData(
            isTokoNow = true,
            isCollapsed = true,
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo
            )
        )

        val newCartDataList = arrayListOf<Any>(
            cartGroupHolderData,
            cartItemHolderData,
            cartItemHolderDataTwo
        )
        val toBeRemovedItems: MutableList<Any> = mutableListOf()

        // WHEN
        cartViewModel.checkAvailableShopBottomHolderData(
            newCartDataList,
            0,
            cartGroupHolderData,
            toBeRemovedItems
        )

        // THEN
        assertTrue(toBeRemovedItems.isEmpty())
        assertEquals(3, newCartDataList.size)
    }

    @Test
    fun `WHEN getShopBottomHolderDataIndex THEN should return group nearest shopBottomHolder`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(cartId = "123", isSelected = true)
        val cartItemHolderDataTwo = CartItemHolderData(cartId = "124", isSelected = false)
        val cartItemHolderDataThree = CartItemHolderData(cartId = "125", isSelected = true)
        val cartGroupHolderData = CartGroupHolderData(
            isCollapsed = true,
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo,
                cartItemHolderDataThree
            )
        )
        val cartShopBottomHolderData = CartShopBottomHolderData(cartGroupHolderData)
        val cartShopBottomHolderDataTwo = CartShopBottomHolderData(CartGroupHolderData())

        val newCartDataList = arrayListOf(
            cartGroupHolderData,
            cartShopBottomHolderData,
            cartShopBottomHolderDataTwo
        )

        // WHEN
        val shopBottomDataPair =
            cartViewModel.getShopBottomHolderDataIndex(newCartDataList, cartGroupHolderData, 0)

        // THEN
        assertEquals(cartShopBottomHolderData, shopBottomDataPair.first)
        assertEquals(1, shopBottomDataPair.second)
    }

    @Test
    fun `WHEN getShopBottomHolderDataIndex not found THEN should return null`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(cartId = "123", isSelected = true)
        val cartItemHolderDataTwo = CartItemHolderData(cartId = "124", isSelected = false)
        val cartItemHolderDataThree = CartItemHolderData(cartId = "125", isSelected = true)
        val cartGroupHolderData = CartGroupHolderData(
            cartString = "123",
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo,
                cartItemHolderDataThree
            )
        )

        val cartGroupHolderDataTwo = CartGroupHolderData(cartString = "124")
        val cartShopBottomHolderDataTwo = CartShopBottomHolderData(cartGroupHolderData)

        val newCartDataList = arrayListOf(
            cartGroupHolderData,
            cartItemHolderData,
            cartItemHolderDataTwo,
            cartItemHolderDataThree,
            cartGroupHolderDataTwo,
            cartShopBottomHolderDataTwo
        )

        // WHEN
        val shopBottomDataPair =
            cartViewModel.getShopBottomHolderDataIndex(newCartDataList, cartGroupHolderData, 0)

        // THEN
        assertEquals(null, shopBottomDataPair.first)
        assertEquals(-1, shopBottomDataPair.second)
    }
    // endregion

    // region setItemSelected
    @Test
    fun `WHEN setItemSelected true to one item in shop THEN all checkbox should checked`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartString = "123",
            isError = false,
            isSelected = false
        )
        val cartGroupHolderData = CartGroupHolderData(
            cartString = "123",
            isError = false,
            productUiModelList = mutableListOf(
                cartItemHolderData
            )
        )
        val cartShopBottomHolderData = CartShopBottomHolderData(cartGroupHolderData)
        cartViewModel.cartDataList.value = arrayListOf(
            cartGroupHolderData,
            cartItemHolderData,
            cartShopBottomHolderData
        )

        // WHEN
        cartViewModel.setItemSelected(1, cartItemHolderData, true)

        // THEN
        val newCartShopBottomHolderData =
            cartViewModel.cartDataList.value[2] as CartShopBottomHolderData
        assertTrue(cartItemHolderData.isSelected)
        assertTrue(cartGroupHolderData.isAllSelected)
        assertFalse(cartGroupHolderData.isPartialSelected)
        assertEquals(
            cartGroupHolderData,
            newCartShopBottomHolderData.shopData
        )
    }

    @Test
    fun `WHEN setItemSelected true to one item in multiple item shop THEN partial checked should true`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartString = "123",
            isError = false,
            isSelected = false
        )
        val cartItemHolderDataTwo = CartItemHolderData(
            cartString = "123",
            isError = false,
            isSelected = false
        )
        val cartGroupHolderData = CartGroupHolderData(
            cartString = "123",
            isError = false,
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo
            )
        )
        cartViewModel.cartDataList.value = arrayListOf(
            cartGroupHolderData,
            cartItemHolderData,
            cartItemHolderDataTwo
        )

        // WHEN
        cartViewModel.setItemSelected(1, cartItemHolderData, true)

        // THEN
        assertTrue(cartItemHolderData.isSelected)
        assertTrue(cartGroupHolderData.isPartialSelected)
        assertFalse(cartGroupHolderData.isAllSelected)
    }

    @Test
    fun `WHEN setItemSelected false to one item in single item shop THEN all checked status should false`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartString = "123",
            isError = false,
            isSelected = true
        )
        val cartGroupHolderData = CartGroupHolderData(
            cartString = "123",
            isError = false,
            productUiModelList = mutableListOf(
                cartItemHolderData
            )
        )
        cartViewModel.cartDataList.value = arrayListOf(
            cartGroupHolderData,
            cartItemHolderData
        )

        // WHEN
        cartViewModel.setItemSelected(1, cartItemHolderData, false)

        // THEN
        assertFalse(cartItemHolderData.isSelected)
        assertFalse(cartGroupHolderData.isPartialSelected)
        assertFalse(cartGroupHolderData.isAllSelected)
    }

    @Test
    fun `WHEN setItemSelected false to one item in multiple item shop THEN all checked status should false`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartString = "123",
            isError = false,
            isSelected = true
        )
        val cartItemHolderDataTwo = CartItemHolderData(
            cartString = "123",
            isError = false,
            isSelected = true
        )
        val cartGroupHolderData = CartGroupHolderData(
            cartString = "123",
            isError = false,
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo
            )
        )
        cartViewModel.cartDataList.value = arrayListOf(
            cartGroupHolderData,
            cartItemHolderData,
            cartItemHolderDataTwo
        )

        // WHEN
        cartViewModel.setItemSelected(1, cartItemHolderData, false)

        // THEN
        assertFalse(cartItemHolderData.isSelected)
        assertFalse(cartGroupHolderData.isAllSelected)
        assertTrue(cartGroupHolderData.isPartialSelected)
    }
    // endregion

    // region addAvailableCartItemImpression
    @Test
    fun `WHEN updateCartGroupFirstItemStatus THEN should update first group and reset other group isFirstItem data`() {
        // GIVEN
        val cartGroupHolderData = CartGroupHolderData(
            isFirstItem = false
        )
        val cartGroupHolderDataTwo = CartGroupHolderData(
            isFirstItem = true
        )
        cartViewModel.cartDataList.value = arrayListOf(
            cartGroupHolderData,
            cartGroupHolderDataTwo
        )

        // WHEN
        cartViewModel.updateCartGroupFirstItemStatus(cartViewModel.cartDataList.value)

        // THEN
        assertTrue(cartGroupHolderData.isFirstItem)
        assertFalse(cartGroupHolderDataTwo.isFirstItem)
    }
    // endregion

    // region addAvailableCartItemImpression
    @Test
    fun `WHEN addAvailableCartItemImpression THEN should add cart item to cart impression list`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(cartId = "123")
        val cartItemHolderDataTwo = CartItemHolderData(cartId = "124")
        val cartItemHolderDataThree = CartItemHolderData(cartId = "125", isError = true)
        val cartImpressionSet = mutableSetOf(cartItemHolderData)

        cartViewModel.cartModel.availableCartItemImpressionList = cartImpressionSet

        // WHEN
        cartViewModel.addAvailableCartItemImpression(
            listOf(
                cartItemHolderData,
                cartItemHolderDataTwo,
                cartItemHolderDataThree
            )
        )

        // THEN
        assertEquals(3, cartViewModel.cartModel.availableCartItemImpressionList.size)
    }
    // endregion

    override fun tearDown() {
        super.tearDown()
        cartViewModel.globalEvent.removeObserver(cartGlobalEventObserver)
        cartViewModel.cartDataList.removeObserver(cartDataListObserver)
        cartViewModel.cartCheckoutButtonState.removeObserver(cartCheckoutButtonStateObserver)
    }
}
