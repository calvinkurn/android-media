package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.*
import io.mockk.mockk
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Irfan Khoirul on 2020-01-31.
 */

object CartListPresenterClickWishlistAnalyticsTest : Spek({

    val view: ICartListView = mockk(relaxed = true)

    Feature("generate wishlist data click analytics") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("1 item selected and cart is not empty") {

            lateinit var result: Map<String, Any>

            When("generate wishlist data click analytics") {
                result = cartListPresenter.generateWishlistProductClickDataLayer(CartWishlistItemHolderData(), 0)
            }

            Then("should be containing 1 product") {
                val add = result[EnhancedECommerceAdd.KEY_ADD] as Map<String, Any>
                val productList = add[EnhancedECommerceAdd.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                Assert.assertEquals(1, productList.size)
            }

            Then("key `list` value should be `cart`") {
                val add = result[EnhancedECommerceAdd.KEY_ADD] as Map<String, Any>
                val actionField = add[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_WISHLIST)
            }

        }

        Scenario("1 item selected and cart is empty") {

            lateinit var result: Map<String, Any>

            When("generate wishlist data click analytics") {
                result = cartListPresenter.generateWishlistProductClickEmptyCartDataLayer(CartWishlistItemHolderData(), 0)
            }

            Then("should be containing 1 product") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val productList = click[EnhancedECommerceCheckout.KEY_PRODUCT] as ArrayList<Map<String, Any>>
                Assert.assertEquals(1, productList.size)
            }

            Then("key `list` value should be `empty cart`") {
                val click = result[EnhancedECommerceCartMapData.KEY_CLICK] as Map<String, Any>
                val actionField = click[EnhancedECommerceCheckout.KEY_ACTION_FIELD] as Map<String, Any>
                Assert.assertTrue((actionField[EnhancedECommerceProductCartMapData.KEY_LIST] as String) == EnhancedECommerceActionField.LIST_WISHLIST_ON_EMPTY_CART)
            }

        }

    }

})