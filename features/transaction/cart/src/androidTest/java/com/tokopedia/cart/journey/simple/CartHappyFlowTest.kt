package com.tokopedia.cart.journey.simple

import android.content.Intent
import android.util.Log
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
        Thread.sleep(10000)

        val cartRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_cart)
        val itemCount = cartRecyclerView.adapter?.itemCount ?: 0
        Log.d("CartHappyFlowTest", "Item Count : " + itemCount)

        cartPage {
            assertMainContent()

            for (i in 0 until itemCount) {
                Log.d("CartHappyFlowTest", "Check position : " + i)
                scrollCartRecyclerViewToPosition(cartRecyclerView, i)
                checkItemType(cartRecyclerView, i, this)
            }
        } buy {

        }

        Thread.sleep(60000)

    }

    private fun checkItemType(cartRecyclerView: RecyclerView, position: Int, cartPageRobot: CartPageRobot) {
        when (cartRecyclerView.findViewHolderForAdapterPosition(position)) {
            is TickerAnnouncementViewHolder -> {
                Log.d("CartHappyFlowTest", "TickerAnnouncementViewHolder")
                cartPageRobot.assertTickerAnnouncementViewHolder(position)
            }
            is CartTickerErrorViewHolder -> {
                Log.d("CartHappyFlowTest", "CartTickerErrorViewHolder")
                val message = String.format(context.getString(com.tokopedia.cart.R.string.cart_error_message), 2)
                cartPageRobot.assertCartTickerErrorViewHolder(position, message)
            }
            is CartShopViewHolder -> {
                Log.d("CartHappyFlowTest", "CartShopViewHolder")
                when (position) {
                    POSITION_FIRST_CART_SHOP_VIEW_HOLDER -> {
                        cartPageRobot.assertFirstCartShopViewHolder(
                                position = position,
                                shopName = "Miniso Indonesia",
                                shopLocation = "Kota Surabaya"
                        )
                    }
                    POSITION_SECOND_CART_SHOP_VIEW_HOLDER -> {
                        cartPageRobot.assertSecondCartShopViewHolder()
                    }
                    POSITION_THIRD_CART_SHOP_VIEW_HOLDER -> {
                        cartPageRobot.assertThirdCartShopViewHolder()
                    }
                }
            }
            is ShipmentSellerCashbackViewHolder -> {
                Log.d("CartHappyFlowTest", "ShipmentSellerCashbackViewHolder")
            }
            is CartSectionHeaderViewHolder -> {
                Log.d("CartHappyFlowTest", "CartSectionHeaderViewHolder")
            }
            is CartEmptyViewHolder -> {
                Log.d("CartHappyFlowTest", "CartEmptyViewHolder")
            }
            is CartRecentViewViewHolder -> {
                Log.d("CartHappyFlowTest", "CartRecentViewViewHolder")
            }
            is CartWishlistViewHolder -> {
                Log.d("CartHappyFlowTest", "CartWishlistViewHolder")
            }
            is CartRecommendationViewHolder -> {
                Log.d("CartHappyFlowTest", "CartRecommendationViewHolder")
            }
            is DisabledItemHeaderViewHolder -> {
                Log.d("CartHappyFlowTest", "DisabledItemHeaderViewHolder")
            }
            is DisabledReasonViewHolder -> {
                Log.d("CartHappyFlowTest", "DisabledReasonViewHolder")
            }
            is DisabledShopViewHolder -> {
                Log.d("CartHappyFlowTest", "DisabledShopViewHolder")
            }
            is DisabledCartItemViewHolder -> {
                Log.d("CartHappyFlowTest", "DisabledCartItemViewHolder")
            }
            is DisabledAccordionViewHolder -> {
                Log.d("CartHappyFlowTest", "DisabledAccordionViewHolder")
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

        const val POSITION_FIRST_CART_SHOP_VIEW_HOLDER = 3
        const val POSITION_SECOND_CART_SHOP_VIEW_HOLDER = 4
        const val POSITION_THIRD_CART_SHOP_VIEW_HOLDER = 5
    }
}