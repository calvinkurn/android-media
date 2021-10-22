package com.tokopedia.cart.bundle.presenter

import com.tokopedia.cart.bundle.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.*
import org.junit.Assert
import org.junit.Test

class ClickWishlistAnalyticsTest : BaseCartTest() {

    @Test
    fun `WHEN generate wishlist data click analytics there's 1 item selected and cart is not empty THEN should be containing 1 product`() {
        // GIVEN
        var result: Map<String, Any>? = null

        // WHEN
        result = cartListPresenter?.generateWishlistProductClickDataLayer(CartWishlistItemHolderData(), 0)

        // THEN
        val add = result?.get(EnhancedECommerceAdd.KEY_ADD) as Map<String, Any>
        val productList = add[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
        Assert.assertEquals(1, productList.size)
    }

    @Test
    fun `WHEN generate wishlist data click analytics there's 1 item selected and cart is not empty THEN key 'list' value should be 'cart'`() {
        // GIVEN
        var result: Map<String, Any>? = null

        // WHEN
        result = cartListPresenter?.generateWishlistProductClickDataLayer(CartWishlistItemHolderData(), 0)

        // THEN
        val add = result?.get(EnhancedECommerceAdd.KEY_ADD) as Map<String, Any>
        val actionField = add[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
        Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_WISHLIST)
    }

    @Test
    fun `WHEN generate wishlist data click analytics there's 1 item selected and cart is empty THEN key 'list' value should be 'empty cart'`() {
        // GIVEN
        var result: Map<String, Any>? = null

        // WHEN
        result = cartListPresenter?.generateWishlistProductClickEmptyCartDataLayer(CartWishlistItemHolderData(), 0)

        // THEN
        val click = result?.get(EnhancedECommerceCartMapData.KEY_CLICK) as Map<String, Any>
        val actionField = click[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
        Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_WISHLIST_ON_EMPTY_CART)
    }

}