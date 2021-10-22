package com.tokopedia.cart.bundle.presenter

import com.tokopedia.cart.bundle.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WishlistImpressionAnalyticsTest : BaseCartTest() {

    @Test
    fun `WHEN generate wishlist impression data analytics with 1 item selected and cart is not empty THEN should be containing 1 product`() {
        // GIVEN
        var result: Map<String, Any>? = null

        val wishlistDataList = mutableListOf<CartWishlistItemHolderData>().apply {
            add(CartWishlistItemHolderData())
        }

        // WHEN
        result = cartListPresenter?.generateWishlistDataImpressionAnalytics(wishlistDataList, false)

        // THEN
        val impression = result?.get(EnhancedECommerceCartMapData.KEY_IMPRESSIONS) as List<Any>
        assertEquals(1, impression.size)
    }

    @Test
    fun `WHEN generate wishlist impression data analytics with 1 item selected and cart is not empty THEN key 'list' value should be 'cart'`() {
        // GIVEN
        var result: Map<String, Any>? = null

        val wishlistDataList = mutableListOf<CartWishlistItemHolderData>().apply {
            add(CartWishlistItemHolderData())
        }

        // WHEN
        result = cartListPresenter?.generateWishlistDataImpressionAnalytics(wishlistDataList, false)

        // THEN
        val impression = result?.get(EnhancedECommerceCartMapData.KEY_IMPRESSIONS) as List<Map<String, Any>>
        assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_WISHLIST)
    }

    @Test
    fun `WHEN generate wishlist impression data analytics with 1 item selected and cart is empty THEN key 'list' value should be 'empty cart'`() {
        // GIVEN
        var result: Map<String, Any>? = null

        val wishlistDataList = mutableListOf<CartWishlistItemHolderData>().apply {
            add(CartWishlistItemHolderData())
        }

        // WHEN
        result = cartListPresenter?.generateWishlistDataImpressionAnalytics(wishlistDataList, true)

        // THEN
        val impression = result?.get(EnhancedECommerceCartMapData.KEY_IMPRESSIONS) as List<Map<String, Any>>
        assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_WISHLIST_ON_EMPTY_CART)
    }
}