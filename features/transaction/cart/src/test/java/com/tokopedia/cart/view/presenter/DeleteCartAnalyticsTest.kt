package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import org.junit.Assert
import org.junit.Test

class DeleteCartAnalyticsTest : BaseCartTest() {

    @Test
    fun `WHEN generate delete cart data analytics with 1 item selected THEN should be containing 1 product`() {
        // GIVEN
        val result: Map<String, Any>?
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData())
        }

        // WHEN
        result = cartListPresenter.generateDeleteCartDataAnalytics(cartItemDataList)

        // THEN
        val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
        val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as List<Any>
        Assert.assertEquals(1, products.size)
    }

    @Test
    fun `WHEN generate delete cart data analytics with 1 item selected and category field is not empty THEN key 'category' value should be 'cat1'`() {
        // GIVEN
        val result: Map<String, Any>?
        val categoryName = "cat1"
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData(category = categoryName))
        }

        // WHEN
        result = cartListPresenter.generateDeleteCartDataAnalytics(cartItemDataList)

        // THEN
        val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
        val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as List<Any>
        val expectedCategoryName = (products[0] as Map<String, Any>)[EnhancedECommerceProductCartMapData.KEY_CAT]
        Assert.assertEquals(categoryName, expectedCategoryName)
    }

    @Test
    fun `WHEN generate delete cart data analytics with1 item selected and tracker attribution field is not empty THEN key 'attribution' should be 'attr'`() {
        // GIVEN
        val result: Map<String, Any>?
        val trackerAttributionValue = "attr"

        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData(trackerAttribution = trackerAttributionValue))
        }

        // WHEN
        result = cartListPresenter.generateDeleteCartDataAnalytics(cartItemDataList)

        // THEN
        val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
        val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as ArrayList<Map<String, Any>>
        val attribution = products[0][EnhancedECommerceProductCartMapData.KEY_ATTRIBUTION]
        Assert.assertTrue(attribution == trackerAttributionValue)
    }

    @Test
    fun `WHEN generate delete cart data analytics with1 item selected and tracker attribution field is not empty THEN key 'dimension 38' should be 'attr'`() {
        // GIVEN
        val result: Map<String, Any>?
        val trackerAttributionValue = "attr"

        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData(trackerAttribution = trackerAttributionValue))
        }

        // WHEN
        result = cartListPresenter.generateDeleteCartDataAnalytics(cartItemDataList)

        // THEN

        val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
        val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as ArrayList<Map<String, Any>>
        val attribution = products[0][EnhancedECommerceProductCartMapData.KEY_DIMENSION_38]
        Assert.assertTrue(attribution == trackerAttributionValue)
    }

    @Test
    fun `WHEN generate delete cart data analytics with 1 item selected and tracker list name field is not empty THEN key 'list' should be 'attr'`() {
        // GIVEN
        val result: Map<String, Any>?
        val trackerListNameValue = "attr"

        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData(trackerListName = trackerListNameValue))
        }

        // WHEN
        result = cartListPresenter.generateDeleteCartDataAnalytics(cartItemDataList)

        // THEN
        val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
        val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as ArrayList<Map<String, Any>>
        val listName = products[0][EnhancedECommerceProductCartMapData.KEY_LIST]
        Assert.assertTrue(listName == trackerListNameValue)
    }

    @Test
    fun `WHEN generate delete cart data analytics with 1 item selected and tracker list name field is not empty THEN key 'dimension 40' should be 'attr'`() {
        // GIVEN
        val result: Map<String, Any>?
        val trackerListNameValue = "attr"

        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData(trackerListName = trackerListNameValue))
        }

        // WHEN
        result = cartListPresenter.generateDeleteCartDataAnalytics(cartItemDataList)

        // THEN
        val action = result.get(key = EnhancedECommerceCartMapData.REMOVE_ACTION) as Map<String, Any>
        val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as ArrayList<Map<String, Any>>
        val attribution = products[0][EnhancedECommerceProductCartMapData.KEY_DIMENSION_40]
        Assert.assertTrue(attribution == trackerListNameValue)
    }

}