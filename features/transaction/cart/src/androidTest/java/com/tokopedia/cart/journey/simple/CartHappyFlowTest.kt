package com.tokopedia.cart.journey.simple

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
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
//        IdlingRegistry.getInstance().register(SimpleIdlingResource.countingIdlingResource)
    }

    private fun createIntent(): Intent {
        return Intent(InstrumentationRegistry.getInstrumentation().targetContext, InstrumentTestCartActivity::class.java)
    }

    @Test
    fun happyFlowTest() {
//        SimpleIdlingResource.countingIdlingResource.dumpStateToLogs()
//        val message: StringBuilder = StringBuilder("Resource: ").append(SimpleIdlingResource.countingIdlingResource.name).append(" inflight transaction iddle: ").append(SimpleIdlingResource.countingIdlingResource.isIdleNow)
//        Log.d("CartHappyFlowTest", message.toString())

        activityRule.launchActivity(createIntent())
        Thread.sleep(1000)

        Log.d("CartHappyFlowTest", "Start Test")
        val cartRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_cart)
        val itemCount = cartRecyclerView.adapter?.itemCount ?: 0
        Log.d("CartHappyFlowTest", "Item Count : " + itemCount)

        cartPage {
            assertMainContent()

            for (i in 0 until itemCount) {
                Log.d("CartHappyFlowTest", "Check position : " + i)
                scrollCartRecyclerViewToPosition(cartRecyclerView, i)
//                Thread.sleep(1000)
                val breakLoop = checkItemType(cartRecyclerView, i, this)
                if (breakLoop) break
            }
        } buy {

        }

//        Thread.sleep(60000)
    }

    private fun checkItemType(cartRecyclerView: RecyclerView, position: Int, cartPageRobot: CartPageRobot): Boolean {
        when (cartRecyclerView.findViewHolderForAdapterPosition(position)) {
            is CartSectionHeaderViewHolder -> {
                return true
            }
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
                                shopLocation = "Kota Surabaya",
                                productName = "MINISO Kotak Penyimpanan Storage Box Container Organizer Tempat Baju - Merah Muda, L",
                                productVariant = "Merah Muda, L"
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
            is CartEmptyViewHolder -> {
                Log.d("CartHappyFlowTest", "CartEmptyViewHolder")
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
        return false
    }

    private fun scrollCartRecyclerViewToPosition(cartRecyclerView: RecyclerView, position: Int) {
        val layoutManager = cartRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    @After
    fun tearDown() {
//        IdlingRegistry.getInstance().unregister(SimpleIdlingResource.countingIdlingResource)
        activityRule.finishActivity()
    }

    companion object {
        const val GET_CART_LIST_KEY = "cart_revamp"

        const val POSITION_FIRST_CART_SHOP_VIEW_HOLDER = 2
        const val POSITION_SECOND_CART_SHOP_VIEW_HOLDER = 3
        const val POSITION_THIRD_CART_SHOP_VIEW_HOLDER = 4
    }
}