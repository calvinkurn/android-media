package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.*
import com.tokopedia.recommendation_widget_common.extension.LABEL_FULFILLMENT
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import org.junit.Assert
import org.junit.Test

class AddToCartRecommendationAnalyticsTest : BaseCartTest() {

    @Test
    fun `WHEN generate add to cart data analytics on recommendation there's 1 item selected on non empty cart THEN should be containing 1 product`() {
        // Given
        val result: Map<String, Any>?

        // When
        result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, RecommendationItem()), AddToCartDataModel(), false)

        // Then
        val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
        val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as List<Any>
        Assert.assertEquals(1, products.size)
    }

    @Test
    fun `WHEN generate add to cart data analytics on recommendation there's 1 item selected on non empty cart THEN key list value should be cart`() {
        // Given
        val result: Map<String, Any>?

        // When
        result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, RecommendationItem()), AddToCartDataModel(), false)

        // Then
        val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
        val actionFields = add[EnhancedECommerceAdd.KEY_ACTION_FIELD] as Map<String, Any>
        Assert.assertTrue((actionFields[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION)
    }

    @Test
    fun `WHEN generate add to cart data analytics on recommendation there's 1 item selected on empty cart THEN should be containing 1 product`() {
        // Given
        val result: Map<String, Any>?

        // When
        result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, RecommendationItem()), AddToCartDataModel(), true)

        // Then
        val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
        val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as List<Any>
        Assert.assertEquals(1, products.size)
    }

    @Test
    fun `WHEN generate add to cart data analytics on recommendation there's 1 item selected on empty cart THEN key list value should be cart`() {
        // Given
        val result: Map<String, Any>?

        // When
        result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, RecommendationItem()), AddToCartDataModel(), true)

        // Then
        val add = result.get(key = EnhancedECommerceCartMapData.ADD_ACTION) as Map<String, Any>
        val actionFields = add[EnhancedECommerceAdd.KEY_ACTION_FIELD] as Map<String, Any>
        Assert.assertTrue((actionFields[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART)
    }

    @Test
    fun `WHEN generate add to cart data analytics on recommendation and eligible for BO THEN dimension 83 should be bebas ongkir`() {
        // Given
        val recommendationItem = RecommendationItem(
                isFreeOngkirActive = true,
                labelGroupList = listOf(RecommendationLabel())
        )
        val result: Map<String, Any>?

        // When
        result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, recommendationItem), AddToCartDataModel(), false)

        // Then
        val add = result.get(key = EnhancedECommerceCartMapData.ADD_ACTION) as Map<String, Any>
        val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
        val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
        Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
    }

    @Test
    fun `WHEN generate add to cart data analytics on recommendation and eligible for BOE THEN dimension 83 should be bebas ongkir extra`() {
        // Given
        val recommendationItem = RecommendationItem(
                isFreeOngkirActive = true,
                labelGroupList = listOf(RecommendationLabel(position = LABEL_FULFILLMENT))
        )
        val result: Map<String, Any>?

        // When
        result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, recommendationItem), AddToCartDataModel(), false)

        // Then
        val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
        val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
        val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
        Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA)
    }

    @Test
    fun `WHEN generate add to cart data analytics on recommendation and not eligible for BO & BOE THEN dimension 83 should be none other`() {
        // Given
        val recommendationItem = RecommendationItem(
                isFreeOngkirActive = false,
                labelGroupList = listOf(RecommendationLabel())
        )
        val result: Map<String, Any>?

        // When
        result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, recommendationItem), AddToCartDataModel(), false)

        // Then
        val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
        val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
        val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
        Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
    }


}