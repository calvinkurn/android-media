package com.tokopedia.cart.view.presenter

import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.*
import com.tokopedia.recommendation_widget_common.extension.LABEL_FULFILLMENT
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import org.junit.Assert
import org.junit.Test

class ClickRecommendationAnalyticsTest : BaseCartTest() {

    @Test
    fun `WHEN generate recommendation data click analytics there's 1 item selected and cart is not empty THEN should be containing 1 product`() {
        // GIVEN
        val result: Map<String, Any>?

        // WHEN
        result = cartListPresenter.generateRecommendationDataOnClickAnalytics(RecommendationItem(), false, 0)

        // THEN
        val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
        val productList = click[EnhancedECommerceCheckout.KEY_PRODUCT] as ArrayList<Map<String, Any>>
        Assert.assertEquals(1, productList.size)
    }

    @Test
    fun `WHEN generate recommendation data click analytics there's 1 item selected and cart is not empty THEN key list value should be cart`() {
        // GIVEN
        val result: Map<String, Any>?

        // WHEN
        result = cartListPresenter.generateRecommendationDataOnClickAnalytics(RecommendationItem(), false, 0)

        // THEN
        val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
        val actionField = click[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
        Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION)
    }

    @Test
    fun `WHEN generate recommendation data click analytics there's 1 item selected and cart is empty THEN key list value should be empty cart`() {
        // GIVEN
        val result: Map<String, Any>?

        // WHEN
        result = cartListPresenter.generateRecommendationDataOnClickAnalytics(RecommendationItem(), true, 0)

        // THEN
        val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
        val actionField = click[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
        Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART)
    }

    @Test
    fun `WHEN generate recommendation data click analytics there's 1 item selected and cart is not empty and item has category breadcrumb THEN key category value should match`() {
        // GIVEN
        val result: Map<String, Any>?
        val categoryBreadcrumb = "cat1/cat2/cat3"

        // WHEN
        result = cartListPresenter.generateRecommendationDataOnClickAnalytics(RecommendationItem(categoryBreadcrumbs = categoryBreadcrumb), false, 0)

        // THEN
        val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
        val products = click[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
        val category = products[0][EnhancedECommerceRecomProductCartMapData.KEY_CAT]
        Assert.assertTrue(category == categoryBreadcrumb)
    }

    @Test
    fun `WHEN generate recommendation data click analytics there's 1 item selected and cart is not empty and eligible for BO THEN dimension 83 should be bebas ongkir`() {
        // GIVEN
        val result: Map<String, Any>?
        val recommendationItem = RecommendationItem(
                isFreeOngkirActive = true,
                labelGroupList = listOf(RecommendationLabel())
        )

        // WHEN
        result = cartListPresenter.generateRecommendationDataOnClickAnalytics(recommendationItem, false, 0)

        // THEN
        val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
        val products = click[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
        val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
        Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
    }

    @Test
    fun `WHEN generate recommendation data click analytics there's 1 item selected and cart is not empty and eligible for BOE THEN dimension 83 should be bebas ongkir extra`() {
        // GIVEN
        val result: Map<String, Any>?
        val recommendationItem = RecommendationItem(
                isFreeOngkirActive = true,
                labelGroupList = listOf(RecommendationLabel(position = LABEL_FULFILLMENT))
        )

        // WHEN
        result = cartListPresenter.generateRecommendationDataOnClickAnalytics(recommendationItem, false, 0)

        // THEN
        val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
        val products = click[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
        val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
        Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA)
    }

    @Test
    fun `WHEN generate recommendation data click analytics there's 1 item selected and cart is not empty and not eligible for BO & BOE THEN dimension 83 should be none other`() {
        // GIVEN
        val result: Map<String, Any>?
        val recommendationItem = RecommendationItem(
                isFreeOngkirActive = false
        )

        // WHEN
        result = cartListPresenter.generateRecommendationDataOnClickAnalytics(recommendationItem, false, 0)

        // THEN
        val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
        val products = click[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
        val dimension83 = products[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
        Assert.assertTrue(dimension83 == EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)

    }
}