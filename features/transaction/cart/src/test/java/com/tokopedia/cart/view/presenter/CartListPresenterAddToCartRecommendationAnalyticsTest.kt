package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceAdd
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import io.mockk.mockk
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Irfan Khoirul on 2020-01-31.
 */

object CartListPresenterAddToCartRecommendationAnalyticsTest : Spek({

    val view: ICartListView = mockk(relaxed = true)

    Feature("generate add to cart data analytics on recommendation") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("1 item selected on non empty cart") {

            lateinit var result: Map<String, Any>

            When("generate add to cart recommendation data analytics") {
                result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, RecommendationItem()), AddToCartDataModel(), false)
            }

            Then("should be containing 1 product") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as List<Any>
                Assert.assertEquals(1, products.size)
            }

            Then("key `list` value should be `cart`") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val actionFields = add[EnhancedECommerceAdd.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionFields[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION)
            }

        }

        Scenario("1 item selected on empty cart") {

            lateinit var result: Map<String, Any>

            When("generate add to cart recommendation data analytics") {
                result = cartListPresenter.generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData(false, RecommendationItem()), AddToCartDataModel(), true)
            }

            Then("should be containing 1 product") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val products = add[EnhancedECommerceAdd.KEY_PRODUCT] as List<Any>
                Assert.assertEquals(1, products.size)
            }

            Then("key `list` value should be `empty cart`") {
                val add = result[EnhancedECommerceCartMapData.ADD_ACTION] as Map<String, Any>
                val actionFields = add[EnhancedECommerceAdd.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionFields[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_CART_RECOMMENDATION_ON_EMPTY_CART)
            }

        }

    }

})