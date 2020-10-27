package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Irfan Khoirul on 2020-01-30.
 */

object CartListPresenterImpressionAnalyticsTest : Spek({

    val view: ICartListView = mockk(relaxed = true)

    Feature("generate recommendation impression data analytics") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("1 item selected and cart is not empty") {

            lateinit var result: Map<String, Any>

            val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
                add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem()))
            }

            When("generate recommendation data analytics") {
                result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, false)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION)
            }

        }

        Scenario("1 item selected and cart is empty") {

            lateinit var result: Map<String, Any>

            val recommendationDataList = mutableListOf<CartRecommendationItemHolderData>().apply {
                add(CartRecommendationItemHolderData(recommendationItem = RecommendationItem()))
            }

            When("generate recommendation data analytics") {
                result = cartListPresenter.generateRecommendationImpressionDataAnalytics(0, recommendationDataList, true)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `empty cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART)
            }

        }

    }

    Feature("generate wishlist impression data analytics") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("1 item selected and cart is not empty") {

            lateinit var result: Map<String, Any>

            val wishlistDataList = mutableListOf<CartWishlistItemHolderData>().apply {
                add(CartWishlistItemHolderData())
            }

            When("generate wishlist data analytics") {
                result = cartListPresenter.generateWishlistDataImpressionAnalytics(wishlistDataList, false)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Any>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_WISHLIST)
            }

        }

        Scenario("1 item selected and cart is empty") {

            lateinit var result: Map<String, Any>

            val wishlistDataList = mutableListOf<CartWishlistItemHolderData>().apply {
                add(CartWishlistItemHolderData())
            }

            When("generate wishlist data analytics") {
                result = cartListPresenter.generateWishlistDataImpressionAnalytics(wishlistDataList, true)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Any>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `empty cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_WISHLIST_ON_EMPTY_CART)
            }

        }

    }

    Feature("generate recent view impression data analytics") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("1 item selected and cart is not empty") {

            lateinit var result: Map<String, Any>

            val recentViewDataList = mutableListOf<CartRecentViewItemHolderData>().apply {
                add(CartRecentViewItemHolderData())
            }

            When("generate recent view data analytics") {
                result = cartListPresenter.generateRecentViewDataImpressionAnalytics(recentViewDataList, false)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Any>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW)
            }

        }

        Scenario("1 item selected and cart is empty") {

            lateinit var result: Map<String, Any>

            val recentViewDataList = mutableListOf<CartRecentViewItemHolderData>().apply {
                add(CartRecentViewItemHolderData())
            }

            When("generate recent view data analytics") {
                result = cartListPresenter.generateRecentViewDataImpressionAnalytics(recentViewDataList, true)
            }

            Then("should be containing 1 product") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Any>
                assertEquals(1, impression.size)
            }

            Then("key `list` value should be `empty cart`") {
                val impression = result[EnhancedECommerceCartMapData.KEY_IMPRESSIONS] as List<Map<String, Any>>
                assertTrue((impression[0][EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_RECENT_VIEW_ON_EMPTY_CART)
            }

        }

    }

})