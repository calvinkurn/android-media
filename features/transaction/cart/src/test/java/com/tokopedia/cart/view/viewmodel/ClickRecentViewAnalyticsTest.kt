package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceAdd
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import org.junit.Assert
import org.junit.Test

class ClickRecentViewAnalyticsTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN generate recent view data click analytics there's 1 item selected and cart is not empty THEN should be containing 1 product`() {
        // GIVEN
        val result: Map<String, Any>?

        // WHEN
        result = cartViewModel.generateRecentViewProductClickDataLayer(CartRecentViewItemHolderData(), 0)

        // THEN
        val add = result[EnhancedECommerceAdd.KEY_ADD] as Map<String, Any>
        val productList = add[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
        Assert.assertEquals(1, productList.size)
    }

    @Test
    fun `WHEN generate recent view data click analytics there's 1 item selected and cart is not empty THEN key list value should be cart`() {
        // GIVEN
        val result: Map<String, Any>?

        // WHEN
        result = cartViewModel.generateRecentViewProductClickDataLayer(CartRecentViewItemHolderData(), 0)

        // THEN
        val add = result[EnhancedECommerceAdd.KEY_ADD] as Map<String, Any>
        val actionField = add[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
        Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW)
    }

    @Test
    fun `WHEN generate recent view data click analytics there's 1 item selected and cart is empty THEN should be containing 1 product`() {
        // GIVEN
        val result: Map<String, Any>?

        // WHEN
        result = cartViewModel.generateRecentViewProductClickEmptyCartDataLayer(CartRecentViewItemHolderData(), 0)

        // THEN
        val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
        val productList = click[EnhancedECommerceCheckout.KEY_PRODUCT] as ArrayList<Map<String, Any>>
        Assert.assertEquals(1, productList.size)
    }

    @Test
    fun `WHEN generate recent view data click analytics there's 1 item selected and cart is empty THEN key list value should be cart`() {
        // GIVEN
        val result: Map<String, Any>?

        // WHEN
        result = cartViewModel.generateRecentViewProductClickEmptyCartDataLayer(CartRecentViewItemHolderData(), 0)

        // THEN
        val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
        val actionField = click[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
        Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW_ON_EMPTY_CART)
    }
}
