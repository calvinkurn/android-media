package com.tokopedia.cart.journey.simple

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cart.robot.cartPage
import com.tokopedia.cart.test.R
import com.tokopedia.cart.view.CartActivity
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class CartBoAffordabilityTest {

    @get:Rule
    var activityRule =
        object : IntentsTestRule<CartActivity>(CartActivity::class.java, false, false) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                InstrumentationAuthHelper.loginInstrumentationTestUser1()
            }
        }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(
                GET_CART_LIST_KEY,
                InstrumentationMockHelper.getRawString(
                    context,
                    R.raw.cart_bo_affordability_response
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                CART_SHOP_GROUP_TICKER_AGGREGATOR_KEY,
                InstrumentationMockHelper.getRawString(
                    context,
                    R.raw.cart_shop_group_ticker_aggregator_bo_afford_success_response
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
        }
    }

    @Test
    fun tickerVisibilityTest() {
        activityRule.launchActivity(null)

        val cartRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_cart)

        cartPage {
            waitForData()

            assertMainContent()

            assertCartShopBottomViewHolderOnPosition(6) {
                // given checked shop & enable bo affordability, then should show ticker
                assertShowCartShopGroupTicker()
            }
            scrollRecyclerViewToPosition(cartRecyclerView, 9)
            assertCartShopBottomViewHolderOnPosition(9) {
                // given unchecked shop & enable bo affordability, then should not show ticker
                assertNotShowCartShopGroupTicker()
            }
            scrollRecyclerViewToPosition(cartRecyclerView, 12)
            assertCartShopBottomViewHolderOnPosition(12) {
                // given checked shop & disable bo affordability, then should not show ticker
                assertNotShowCartShopGroupTicker()
            }

            // Prevent glide crash
            Thread.sleep(2000)
        }
    }

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    @After
    fun tearDown() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }

    companion object {
        const val GET_CART_LIST_KEY = "cart_revamp"
        const val CART_SHOP_GROUP_TICKER_AGGREGATOR_KEY = "cartShopGroupTickerAggregator"
    }
}
