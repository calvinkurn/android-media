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

class CartBebasOngkirExtraUITest {

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
            addMockResponse(GET_CART_LIST_KEY, InstrumentationMockHelper.getRawString(context, R.raw.cart_bebas_ongkir_extra_response), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun happyFlowTest() {
        activityRule.launchActivity(null)

        cartPage {
            waitForData()

            assertMainContent()

            assertCartShopViewHolderOnPosition(2) {
                assertShowTokoCabangInfo()
                assertShowFreeShippingImage()
            }
            assertCartShopViewHolderOnPosition(3) {
                assertNotShowTokoCabangInfo()
                assertShowFreeShippingImage()
            }
            assertCartShopViewHolderOnPosition(4) {
                assertNotShowTokoCabangInfo()
                assertNotShowFreeShippingImage()
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
    }
}