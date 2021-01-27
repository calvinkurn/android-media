package com.tokopedia.cart.journey.simple

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cart.InstrumentTestCartActivity
import com.tokopedia.cart.robot.CartPageRobot
import com.tokopedia.cart.robot.cartPage
import com.tokopedia.cart.test.R
import com.tokopedia.cart.view.viewholder.CartSectionHeaderViewHolder
import com.tokopedia.cart.view.viewholder.CartShopViewHolder
import com.tokopedia.cart.view.viewholder.CartTickerErrorViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartHappyFlowTest {

    @get:Rule
    val activityRule = IntentsTestRule(InstrumentTestCartActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(GET_CART_LIST_KEY, InstrumentationMockHelper.getRawString(context, R.raw.cart_happy_flow_response), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    private fun createIntent(): Intent {
        return Intent(InstrumentationRegistry.getInstrumentation().targetContext, InstrumentTestCartActivity::class.java)
    }

    @Test
    fun happyFlowTest() {
        activityRule.launchActivity(createIntent())
        Thread.sleep(10000)

        val cartRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_cart)
        val itemCount = cartRecyclerView.adapter?.itemCount ?: 0

        cartPage {
            initData(context)
            assertMainContent()

            for (i in 0 until itemCount) {
                scrollCartRecyclerViewToPosition(cartRecyclerView, i)
                val breakLoop = checkItemType(cartRecyclerView, i, this)
                if (breakLoop) break
            }
        }
    }

    private fun checkItemType(cartRecyclerView: RecyclerView, position: Int, cartPageRobot: CartPageRobot): Boolean {
        when (cartRecyclerView.findViewHolderForAdapterPosition(position)) {
            is CartSectionHeaderViewHolder -> {
                return true
            }
            is TickerAnnouncementViewHolder -> {
                cartPageRobot.assertTickerAnnouncementViewHolder(position)
            }
            is CartTickerErrorViewHolder -> {
                val message = String.format(context.getString(com.tokopedia.cart.R.string.cart_error_message), 2)
                cartPageRobot.assertCartTickerErrorViewHolder(position, message)
            }
            is CartShopViewHolder -> {
                when (position) {
                    POSITION_FIRST_CART_SHOP_VIEW_HOLDER -> {
                        cartPageRobot.assertFirstCartShopViewHolder(
                                view = activityRule.activity.findViewById(R.id.parent_view_cart),
                                position = position
                        )
                    }
                }
            }
        }
        return false
    }

    private fun scrollCartRecyclerViewToPosition(cartRecyclerView: RecyclerView, position: Int) {
        val layoutManager = cartRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    @After
    fun tearDown() {
        activityRule.finishActivity()
    }

    companion object {
        const val GET_CART_LIST_KEY = "cart_revamp"

        const val POSITION_FIRST_CART_SHOP_VIEW_HOLDER = 1
        const val POSITION_SECOND_CART_SHOP_VIEW_HOLDER = 2
        const val POSITION_THIRD_CART_SHOP_VIEW_HOLDER = 3
    }
}