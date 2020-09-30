package com.tokopedia.cart.journey.simple

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cart.InstrumentTestCartActivity
import com.tokopedia.cart.robot.CartPageRobot
import com.tokopedia.cart.robot.cartPage
import com.tokopedia.cart.test.R
import com.tokopedia.cart.utils.SimpleIdlingResource
import com.tokopedia.cart.view.viewholder.*
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackViewHolder
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
        activityRule.launchActivity(createIntent())
        IdlingRegistry.getInstance().register(SimpleIdlingResource.countingIdlingResource)
    }

    private fun createIntent(): Intent {
        return Intent(InstrumentationRegistry.getInstrumentation().targetContext, InstrumentTestCartActivity::class.java)
    }

    @Test
    fun happyFlowTest() {
        val cartRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_cart)
        val itemCount = cartRecyclerView.adapter?.itemCount ?: 0

        cartPage {
            for (i in 0 until itemCount) {
                scrollCartRecyclerViewToPosition(cartRecyclerView, i)
                checkItemType(cartRecyclerView, i)
            }
        } buy {

        }

    }

    private fun checkItemType(cartRecyclerView: RecyclerView, position: Int, cartPageRobot: CartPageRobot) {
        when (cartRecyclerView.findViewHolderForAdapterPosition(position)) {
            is TickerAnnouncementViewHolder -> {
                cartPageRobot.assertTickerAnnouncement()
            }
            is CartTickerErrorViewHolder -> {
                cartPageRobot.assertTickerError()
            }
            is CartShopViewHolder -> {

            }
            is ShipmentSellerCashbackViewHolder -> {

            }
            is CartSectionHeaderViewHolder -> {

            }
            is CartEmptyViewHolder -> {

            }
            is CartRecentViewViewHolder -> {

            }
            is CartWishlistViewHolder -> {

            }
            is CartRecommendationViewHolder -> {

            }
            is DisabledItemHeaderViewHolder -> {

            }
            is DisabledReasonViewHolder -> {

            }
            is DisabledShopViewHolder -> {

            }
            is DisabledCartItemViewHolder -> {

            }
            is DisabledAccordionViewHolder -> {

            }
        }
    }

    private fun scrollCartRecyclerViewToPosition(cartRecyclerView: RecyclerView, position: Int) {
        val layoutManager = cartRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(SimpleIdlingResource.countingIdlingResource)
    }

    companion object {
        const val GET_CART_LIST_KEY = "cart_revamp"
    }
}