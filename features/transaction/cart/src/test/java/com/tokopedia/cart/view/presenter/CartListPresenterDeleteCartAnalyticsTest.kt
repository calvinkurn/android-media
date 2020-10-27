package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import io.mockk.mockk
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Irfan Khoirul on 2020-01-31.
 */

object CartListPresenterDeleteCartAnalyticsTest : Spek({

    val view: ICartListView = mockk(relaxed = true)

    Feature("generate delete cart data analytics") {

        val cartListPresenter by memoized {
            PresenterProvider.provideCartListPresenter()
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("1 item selected") {

            lateinit var result: Map<String, Any>

            val cartItemDataList = mutableListOf<CartItemData>().apply {
                add(CartItemData())
            }

            When("generate cart data analytics") {
                result = cartListPresenter.generateDeleteCartDataAnalytics(cartItemDataList)
            }

            Then("should be containing 1 product") {
                val action = result[EnhancedECommerceCartMapData.REMOVE_ACTION] as Map<String, Any>
                val products = action[EnhancedECommerceCartMapData.KEY_PRODUCTS] as List<Any>
                Assert.assertEquals(1, products.size)
            }

        }

    }

})