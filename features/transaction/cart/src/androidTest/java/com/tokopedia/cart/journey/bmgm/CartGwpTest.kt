package com.tokopedia.cart.journey.bmgm

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cart.CartActivity
import com.tokopedia.cart.interceptor.CART_GWP_RESPONSE_PATH
import com.tokopedia.cart.interceptor.CartInterceptors
import com.tokopedia.cart.interceptor.CartInterceptors.cartInterceptor
import com.tokopedia.cart.robot.cartPage
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.cart.test.R as carttestR

@UiTest
class CartGwpTest {

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
        CartInterceptors.setupGraphqlMockResponse(context)
        CartInterceptors.resetAllCustomResponse()
    }

    @Test
    fun happyFlowTest_gwpWithGift() {
        cartInterceptor.customGetCartResponsePath = CART_GWP_RESPONSE_PATH
        activityRule.launchActivity(null)

        val cartRecyclerView =
            activityRule.activity.findViewById<RecyclerView>(carttestR.id.rv_cart)

        cartPage {
            waitForData()

            initData(context, carttestR.raw.cart_owoc_multi_response)
            assertMainContent()

            scrollRecyclerViewToPosition(recyclerView = cartRecyclerView, position = 2)
            Thread.sleep(2000)
            assertCartItemOnPosition(
                position = 5
            ) {
                assertGwpWidget(false)
            }
            assertCartItemOnPosition(
                position = 6
            ) {
                assertGwpWidget(true)
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
}
