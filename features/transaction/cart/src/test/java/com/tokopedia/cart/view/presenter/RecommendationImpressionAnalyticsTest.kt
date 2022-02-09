package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceRecomProductCartMapData
import com.tokopedia.recommendation_widget_common.extension.LABEL_FULFILLMENT
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecommendationImpressionAnalyticsTest : BaseCartTest() {

    @Test
    fun `WHEN generate recommendation impression data analytics with 1 item selected and cart is not empty THEN should be containing 1 product`() {
        // GIVEN
        val result: Map<String, Any>?

        val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
            add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem()))
        }

        // WHEN
        result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)

        // THEN
        val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
        assertEquals(1, impression.size)
    }

    @Test
    fun `WHEN generate recommendation impression data analytics with 1 item selected and cart is not empty THEN key 'list' value should be 'cart'`() {
        // GIVEN
        val result: Map<String, Any>?

        val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
            add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem()))
        }

        // WHEN
        result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)

        // THEN
        val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
        assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION)
    }

    @Test
    fun `WHEN generate recommendation impression data analytics with 1 top ads item selected and cart is not empty THEN key 'list' value should be 'top ads'`() {
        // GIVEN
        val result: Map<String, Any>?

        val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
            add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem(isTopAds = true)))
        }

        // WHEN
        result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)

        // THEN
        val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
        assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String).contains(EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_TOPADS_TYPE))
    }

    @Test
    fun `WHEN generate recommendation impression data analytics with 1 item selected and cart is not empty THEN key 'category' should be 'cat1cat2cat3'`() {
        //GIVEN
        val result: Map<String, Any>?
        val categoryBreadcrumb = "cat1/cat2/cat3"

        val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
            add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem(categoryBreadcrumbs = categoryBreadcrumb)))
        }

        // WHEN
        result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)

        // THEN
        val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as ArrayList<Map<String, Any>>
        val category = impression[0][EnhancedECommerceRecomProductCartMapData.KEY_CAT]
        assertTrue(category == categoryBreadcrumb)
    }

    @Test
    fun `WHEN generate recommendation impression data analytics with 1 item selected and eligible for BO THEN dimension 83 should be 'bebas ongkir'`() {
        //GIVEN
        val result: Map<String, Any>?

        val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
            add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem(isFreeOngkirActive = true)))
        }

        // WHEN
        result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)

        // THEN
        val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as ArrayList<Map<String, Any>>
        val dimension83 = impression[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
        assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR)
    }

    @Test
    fun `WHEN generate recommendation impression data analytics with 1 item selected and eligible for BOE THEN dimension 83 should be 'bebas ongkir extra'`() {
        //GIVEN
        val result: Map<String, Any>?

        val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
            add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem(isFreeOngkirActive = true, labelGroupList = arrayListOf(RecommendationLabel(position = LABEL_FULFILLMENT)))))
        }

        // WHEN
        result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)

        // THEN
        val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as ArrayList<Map<String, Any>>
        val dimension83 = impression[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
        assertTrue(dimension83 == EnhancedECommerceProductCartMapData.VALUE_BEBAS_ONGKIR_EXTRA)

    }

    @Test
    fun `WHEN generate recommendation impression data analytics with 1 item selected and not eligible for BO & BOE THEN dimension 83 should be 'none other'`() {
        // GIVEN
        val result: Map<String, Any>?

        val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
            add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem()))
        }

        // WHEN
        result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)

        // THEN
        val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as ArrayList<Map<String, Any>>
        val dimension83 = impression[0][EnhancedECommerceRecomProductCartMapData.KEY_DIMENSION_83]
        assertTrue(dimension83 == EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)

    }

    @Test
    fun `WHEN generate recommendation impression data analytics with 1 item selected and cart is empty THEN key 'list' value should be ' empty cart'`() {
        // GIVEN
        val result: Map<String, Any>?

        val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
            add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem()))
        }

        // WHEN
        result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, true)

        // THEN
        val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
        assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART)
    }
}