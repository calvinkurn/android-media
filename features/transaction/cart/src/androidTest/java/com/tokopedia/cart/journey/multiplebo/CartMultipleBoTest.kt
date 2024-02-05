package com.tokopedia.cart.journey.multiplebo

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cart.CartActivity
import com.tokopedia.cart.robot.cartPage
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.cart.test.R as carttestR

class CartHappyFlowTest {

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
            addMockResponse(GET_CART_LIST_KEY, InstrumentationMockHelper.getRawString(context, carttestR.raw.cart_mulitple_bo_response), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun happyFlowTest() {
        activityRule.launchActivity(null)

        val cartRecyclerView = activityRule.activity.findViewById<RecyclerView>(carttestR.id.rv_cart)

        cartPage {
            waitForData()

            initData(context, carttestR.raw.cart_mulitple_bo_response)
            assertMainContent()

            scrollRecyclerViewToPosition(recyclerView = cartRecyclerView, position = 0)
//            assertCartSelectAllViewHolder()

            scrollRecyclerViewToPosition(recyclerView = cartRecyclerView, position = 1)
            assertTickerAnnouncementViewHolder(position = 1)

            scrollRecyclerViewToPosition(recyclerView = cartRecyclerView, position = 2)
            assertFirstCartGroupViewHolder(
                position = 3,
                shopIndex = 0
            )

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
    }
}
