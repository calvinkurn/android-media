package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecentViewImpressionAnalyticsTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN generate recent view impression data analytics with 1 item selected and cart is not empty THEN should be containing 1 product`() {
        // GIVEN
        val result: Map<String, Any>?

        val recentViewDataList = mutableListOf<CartRecentViewItemHolderData>().apply {
            add(CartRecentViewItemHolderData())
        }

        // WHEN
        result = cartViewModel.generateRecentViewDataImpressionAnalytics(recentViewDataList, false)

        // THEN
        val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Any>
        assertEquals(1, impression.size)
    }

    @Test
    fun `WHEN generate recent view impression data analytics with 1 item selected and cart is not empty THEN key 'list' value should be 'cart'`() {
        // GIVEN
        val result: Map<String, Any>?

        val recentViewDataList = mutableListOf<CartRecentViewItemHolderData>().apply {
            add(CartRecentViewItemHolderData())
        }

        // WHEN
        result = cartViewModel.generateRecentViewDataImpressionAnalytics(recentViewDataList, false)

        // THEN
        val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
        assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW)
    }

    @Test
    fun `WHEN generate recent view impression data analytics with 1 item selected and cart is empty THEN key 'list' value should be 'empty cart'`() {
        // GIVEN
        val result: Map<String, Any>?

        val recentViewDataList = mutableListOf<CartRecentViewItemHolderData>().apply {
            add(CartRecentViewItemHolderData())
        }

        // WHEN
        result = cartViewModel.generateRecentViewDataImpressionAnalytics(recentViewDataList, true)

        // THEN
        val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
        assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW_ON_EMPTY_CART)
    }
}
