package com.tokopedia.cart.journey.simple

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cart.robot.cartPage
import com.tokopedia.cart.test.R
import com.tokopedia.cart.view.CartActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartBoAffordabilityTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<CartActivity>(CartActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(GET_CART_LIST_KEY, InstrumentationMockHelper.getRawString(context, R.raw.cart_bo_affordability_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(ONGKIR_GET_FREE_SHIPPING_KEY, InstrumentationMockHelper.getRawString(context, R.raw.ongkir_get_free_shipping_success_afford_response), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun tickerVisibilityTest() {
        activityRule.launchActivity(null)

        cartPage {
            waitForData()

            assertMainContent()

            assertCartShopViewHolderOnPosition(3) {
                // given checked shop & enable bo affordability, then should show ticker
                assertShowBoAffordabilityTicker()
            }
            assertCartShopViewHolderOnPosition(4) {
                // given unchecked shop & enable bo affordability, then should not show ticker
                assertNotShowBoAffordabilityTicker()
            }
            assertCartShopViewHolderOnPosition(5) {
                // given checked shop & disable bo affordability, then should not show ticker
                assertNotShowBoAffordabilityTicker()
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
        const val ONGKIR_GET_FREE_SHIPPING_KEY = "ongkirGetFreeShipping"
    }
}