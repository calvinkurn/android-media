package com.tokopedia.cart.journey.simple

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
class CartBundlingTest {

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
                InstrumentationMockHelper.getRawString(context, R.raw.cart_bundling_response),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                CART_SHOP_GROUP_TICKER_AGGREGATOR_KEY,
                InstrumentationMockHelper.getRawString(
                    context,
                    R.raw.cart_shop_group_ticker_aggregator_cart_bundling_success_response
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
        }
    }

    @Test
    fun bundlingTickerVisibilityTest() {
        activityRule.launchActivity(null)

        cartPage {
            waitForData()

            assertMainContent()

            assertCartShopBottomViewHolderOnPosition(4) {
                // given checked shop, normal product with no bundleIds, & enable cart bundling
                // then should not show ticker
                assertNotShowCartShopGroupTicker()
            }
            assertCartShopBottomViewHolderOnPosition(7) {
                // given checked shop, normal product with bundleIds, & enable cart bundling
                // then should show ticker
                assertShowCartShopGroupTicker()
            }

            // Prevent glide crash
            Thread.sleep(2000)
        }
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
