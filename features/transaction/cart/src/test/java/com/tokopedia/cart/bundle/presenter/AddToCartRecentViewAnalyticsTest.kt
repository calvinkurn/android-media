package com.tokopedia.cart.bundle.presenter

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cart.bundle.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceAdd
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import org.junit.Assert
import org.junit.Test

class AddToCartRecentViewAnalyticsTest : BaseCartTest() {

    @Test
    fun `WHEN generate add to cart data analytics on recent view there's 1 item selected on non empty cart THEN should be containing 1 product`() {
        // Given
        var result: Map<String, Any>? = null

        // When
        result = cartListPresenter?.generateAddToCartEnhanceEcommerceDataLayer(CartRecentViewItemHolderData(), AddToCartDataModel(), false)

        // Then
        Assert.assertTrue(result != null)
        val add = result?.get(EnhancedECommerceCartMapData.ADD_ACTION) as Map<String, Any>
        val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as List<Any>
        Assert.assertEquals(1, products.size)
    }

    @Test
    fun `WHEN generate add to cart data analytics on recent view there's 1 item selected on non empty cart THEN key list value should be cart`() {
        // Given
        var result: Map<String, Any>? = null

        // When
        result = cartListPresenter?.generateAddToCartEnhanceEcommerceDataLayer(CartRecentViewItemHolderData(), AddToCartDataModel(), false)

        // Then
        val add = result?.get(EnhancedECommerceCartMapData.ADD_ACTION) as Map<String, Any>
        val actionFields = add[EnhancedECommerceAdd.KEY_ACTION_FIELD] as Map<String, Any>
        Assert.assertTrue((actionFields[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW)
    }

    @Test
    fun `WHEN generate add to cart data analytics on recent view there's 1 item selected on empty cart THEN should be containing 1 product`() {
        // Given
        var result: Map<String, Any>? = null

        // When
        result = cartListPresenter?.generateAddToCartEnhanceEcommerceDataLayer(CartRecentViewItemHolderData(), AddToCartDataModel(), true)

        // Then
        Assert.assertTrue(result != null)
        val add = result?.get(EnhancedECommerceCartMapData.ADD_ACTION) as Map<String, Any>
        val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as List<Any>
        Assert.assertEquals(1, products.size)
    }

    @Test
    fun `WHEN generate add to cart data analytics on recent view there's 1 item selected on empty cart THEN key list value should be empty cart`() {
        // Given
        var result: Map<String, Any>? = null

        // When
        result = cartListPresenter?.generateAddToCartEnhanceEcommerceDataLayer(CartRecentViewItemHolderData(), AddToCartDataModel(), true)

        // Then
        val add = result?.get(EnhancedECommerceCartMapData.ADD_ACTION) as Map<String, Any>
        val actionFields = add[EnhancedECommerceAdd.KEY_ACTION_FIELD] as Map<String, Any>
        Assert.assertTrue((actionFields[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW_ON_EMPTY_CART)
    }

}