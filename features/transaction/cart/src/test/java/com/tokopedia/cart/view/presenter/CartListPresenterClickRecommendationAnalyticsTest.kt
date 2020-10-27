package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import io.mockk.mockk
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Irfan Khoirul on 2020-01-31.
 */

object CartListPresenterClickRecommendationAnalyticsTest : Spek({

    val view: ICartListView = mockk(relaxed = true)

    Feature("generate recommendation data click analytics") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("1 item selected and cart is not empty") {

            lateinit var result: Map<String, Any>

            When("generate recommendation data click analytics") {
                result = cartListPresenter.generateRecommendationDataOnClickAnalytics(RecommendationItem(), false, 0)
            }

            Then("should be containing 1 product") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val productList = click[EnhancedECommerceCheckout.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                Assert.assertEquals(1, productList.size)
            }

            Then("key `list` value should be `cart`") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val actionField = click[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION)
            }

        }

        Scenario("1 item selected and cart is empty") {

            lateinit var result: Map<String, Any>

            When("generate recommendation data click analytics") {
                result = cartListPresenter.generateRecommendationDataOnClickAnalytics(RecommendationItem(), true, 0)
            }

            Then("should be containing 1 product") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val productList = click[EnhancedECommerceCheckout.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                Assert.assertEquals(1, productList.size)
            }

            Then("key `list` value should be `empty cart`") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val actionField = click[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART)
            }

        }

    }

})