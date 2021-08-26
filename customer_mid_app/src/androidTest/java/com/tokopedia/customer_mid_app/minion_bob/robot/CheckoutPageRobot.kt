package com.tokopedia.customer_mid_app.minion_bob.robot

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.cart.view.viewholder.CartItemViewHolder
import com.tokopedia.cart.view.viewholder.CartSelectAllViewHolder
import com.tokopedia.cart.view.viewholder.CartShopViewHolder
import com.tokopedia.cart.view.viewholder.CartTickerErrorViewHolder
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.viewholder.PromoCheckoutViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentButtonPaymentViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentItemViewHolder
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.isA
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

fun checkoutPage(func: CheckoutPageRobot.() -> Unit) = CheckoutPageRobot().apply(func)

class CheckoutPageRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    private fun <T : Activity> scrollRecyclerViewToFirstOrder(activityRule: IntentsTestRule<T>): Int {
        onView(withId(R.id.rv_shipment)).perform(RecyclerViewActions.scrollToHolder(isA(ShipmentItemViewHolder::class.java)))
        return 1
//        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_shipment)
//        val itemCount = recyclerView.adapter?.itemCount ?: 0
//
//        var position = RecyclerView.NO_POSITION
//        for (i in 0 until itemCount) {
//            scrollRecyclerViewToPosition(activityRule, recyclerView, i)
//            when (recyclerView.findViewHolderForAdapterPosition(i)) {
//                is ShipmentItemViewHolder -> {
//                    position = i
//                    break
//                }
//            }
//        }
//
//        return position
    }

    private fun <T : Activity> scrollRecyclerViewToChoosePaymentButton(activityRule: IntentsTestRule<T>): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_shipment)
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        var position = RecyclerView.NO_POSITION
        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(activityRule, recyclerView, i)
            when (recyclerView.findViewHolderForAdapterPosition(i)) {
                is ShipmentButtonPaymentViewHolder -> {
                    position = i
                    break
                }
            }
        }

        return position
    }

    private fun <T : Activity> scrollRecyclerViewToPromoButton(activityRule: IntentsTestRule<T>): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_shipment)
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        var position = RecyclerView.NO_POSITION
        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(activityRule, recyclerView, i)
            when (recyclerView.findViewHolderForAdapterPosition(i)) {
                is PromoCheckoutViewHolder -> {
                    position = i
                    break
                }
            }
        }

        return position
    }

    private fun <T : Activity> scrollRecyclerViewToPosition(activityRule: IntentsTestRule<T>,
                                                            recyclerView: RecyclerView,
                                                            position: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = ViewActions.click().perform(uiController, view.findViewById(viewId))
    }

    fun <T : Activity> clickChooseDuration(activityRule: IntentsTestRule<T>) {
//        val position = scrollRecyclerViewToFirstOrder(activityRule)
//        if (position != RecyclerView.NO_POSITION) {
//            onView(ViewMatchers.withId(R.id.rv_shipment))
//                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position,
//                            clickOnViewChild(R.id.layout_state_no_selected_shipping)))
            onView(ViewMatchers.withId(R.id.rv_shipment))
                    .perform(RecyclerViewActions.actionOnHolderItem(isA(ShipmentItemViewHolder::class.java),
                            clickOnViewChild(R.id.layout_state_no_selected_shipping)))
//        }
    }

    fun selectFirstShippingDurationOption() {
        onView(ViewMatchers.withText("Bebas Ongkir")).perform(ViewActions.click())
    }

    fun <T : Activity> clickPromoButton(activityRule: IntentsTestRule<T>) {
        val position = scrollRecyclerViewToPromoButton(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_shipment))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position,
                            clickOnViewChild(R.id.promo_checkout_btn_shipment)))
        }
    }

    fun <T : Activity> clickChoosePaymentButton(activityRule: IntentsTestRule<T>) {
//        val position = scrollRecyclerViewToChoosePaymentButton(activityRule)
//        if (position != RecyclerView.NO_POSITION) {
//            onView(ViewMatchers.withId(R.id.rv_shipment))
//                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position,
//                            clickOnViewChild(R.id.btn_select_payment_method)))
            onView(ViewMatchers.withId(R.id.rv_shipment))
                    .perform(RecyclerViewActions.actionOnHolderItem(isA(ShipmentButtonPaymentViewHolder::class.java),
                            clickOnViewChild(R.id.btn_select_payment_method)))
//        }
    }

    infix fun validateAnalytics(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }

    /**
     * Assert Single Shipment State UI
     *
     * @param title Shipment title with postfix " (", ex: "Pengiriman 2 jam (" or "Pengiriman 2 jam (Rp0)
     * @param originalPrice if discounted price is not equal, ex: "Rp20.000"
     * @param discountedPrice additional price with prefix space and postfix ")" if available, ex: " Rp5.000)"
     * @param eta eta message
     * @param message additional promo message if available
     */
    fun <T : Activity> assertHasSingleShipmentSelected(activityRule: IntentsTestRule<T>, title: String, originalPrice: String? = null,
                                        discountedPrice: String? = null, eta: String, message: String? = null) {
        val position = scrollRecyclerViewToFirstOrder(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_shipment))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, object : ViewAction {
                        override fun getConstraints(): Matcher<View>? = null

                        override fun getDescription(): String = "Assert Single Shipment Selected UI"

                        override fun perform(uiController: UiController?, view: View) {
                            assertEquals(View.VISIBLE, view.findViewById<View>(R.id.layout_state_has_selected_single_shipping).visibility)
                            assertEquals(title, view.findViewById<Typography>(R.id.label_selected_single_shipping_title).text)
                            if (originalPrice != null) {
                                assertEquals(originalPrice, view.findViewById<Typography>(R.id.label_selected_single_shipping_original_price).text)
                                assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.label_selected_single_shipping_original_price).visibility)
                            } else {
                                assertEquals(View.GONE, view.findViewById<Typography>(R.id.label_selected_single_shipping_original_price).visibility)
                            }
                            if (discountedPrice != null) {
                                assertEquals(discountedPrice, view.findViewById<Typography>(R.id.label_selected_single_shipping_discounted_price).text)
                                assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.label_selected_single_shipping_original_price).visibility)
                            } else {
                                assertEquals(View.GONE, view.findViewById<Typography>(R.id.label_selected_single_shipping_discounted_price).visibility)
                            }
                            assertEquals(eta, view.findViewById<Typography>(R.id.label_single_shipping_eta).text)
                            if (message != null) {
                                assertEquals(message, view.findViewById<Typography>(R.id.label_single_shipping_message).text.toString())
                                assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.label_single_shipping_message).visibility)
                            } else {
                                assertEquals(View.GONE, view.findViewById<Typography>(R.id.label_single_shipping_message).visibility)
                            }
                        }
                    }))
        }
    }
}

class ResultRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun hasPassedAnalytics(gtmLogDBSource: GtmLogDBSource, context: Context, queryFileName: String) {
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, queryFileName), hasAllSuccess())
    }

    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
//        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, queryFileName), hasAllSuccess())
        assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
    }

    fun assertGoToPayment() {
        val paymentPassData = Intents.getIntents().last().getParcelableExtra<PaymentPassData>(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)
        assertNotNull(paymentPassData)
    }
}

fun cartPage(func: CartPageRobot.() -> Unit) = CartPageRobot().apply(func)

class CartPageRobot {

    var cartListData: CartListData? = null

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun assertMainContent() {
        onView(withId(com.tokopedia.cart.R.id.ll_cart_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(com.tokopedia.cart.R.id.rv_cart)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun assertTickerAnnouncementViewHolder(position: Int) {
        onView(withId(com.tokopedia.cart.R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<TickerAnnouncementViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on TickerAnnouncementViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Assert.assertEquals(View.VISIBLE, view.findViewById<Ticker>(com.tokopedia.cart.R.id.cartTicker).visibility)
            }
        }))
    }

    fun assertCartSelectAllViewHolder(position: Int) {
        onView(withId(com.tokopedia.cart.R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<CartSelectAllViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on CartSelectAllViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Assert.assertEquals(View.VISIBLE, view.findViewById<CheckboxUnify>(com.tokopedia.cart.R.id.checkbox_global).visibility)
                Assert.assertEquals(true, view.findViewById<CheckboxUnify>(com.tokopedia.cart.R.id.checkbox_global).isChecked)
                Assert.assertEquals(View.VISIBLE, view.findViewById<Typography>(com.tokopedia.cart.R.id.text_select_all).visibility)
            }
        }))
    }

    fun assertCartTickerErrorViewHolder(position: Int, message: String) {
        onView(withId(com.tokopedia.cart.R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<CartTickerErrorViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on CartTickerErrorViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Assert.assertEquals(message, view.findViewById<Typography>(com.tokopedia.cart.R.id.ticker_description).text)
            }
        }))
    }

    fun assertFirstCartShopViewHolder(view: View,
                                      position: Int,
                                      shopIndex: Int) {
        onView(withId(com.tokopedia.cart.R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<CartShopViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on first CartShopViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Assert.assertEquals(cartListData?.shopGroupAvailableDataList?.get(shopIndex)?.shopName
                        ?: "", view.findViewById<Typography>(com.tokopedia.cart.R.id.tv_shop_name).text)
                Assert.assertEquals(cartListData?.shopGroupAvailableDataList?.get(shopIndex)?.fulfillmentName
                        ?: "", view.findViewById<Typography>(com.tokopedia.cart.R.id.tv_fulfill_district).text)
                Assert.assertEquals(View.VISIBLE, view.findViewById<ImageView>(com.tokopedia.cart.R.id.image_shop_badge).visibility)
                Assert.assertEquals(View.VISIBLE, view.findViewById<ImageUnify>(com.tokopedia.cart.R.id.img_free_shipping).visibility)
                Assert.assertEquals(View.VISIBLE, view.findViewById<RecyclerView>(com.tokopedia.cart.R.id.rv_cart_item).visibility)
                Assert.assertTrue(view.findViewById<CheckBox>(com.tokopedia.cart.R.id.cb_select_shop).isChecked)
            }
        }))

        assertOnEachCartItem(
                view = view,
                recyclerViewId = com.tokopedia.cart.R.id.rv_cart_item,
                shopIndex = shopIndex
        )

    }

    fun assertOnEachCartItem(view: View, recyclerViewId: Int, shopIndex: Int) {
        val childRecyclerView: RecyclerView = view.findViewById(recyclerViewId)

        val tempStoreDesc = childRecyclerView.contentDescription
        childRecyclerView.contentDescription = CommonActions.UNDER_TEST_TAG

        val childItemCount = childRecyclerView.adapter?.itemCount ?: 0
        for (i in 0 until childItemCount) {
            try {
                onView(allOf(withId(recyclerViewId), withContentDescription(CommonActions.UNDER_TEST_TAG)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<CartItemViewHolder>(i, object : ViewAction {
                            override fun getDescription(): String = "performing assertion action on first CartShopViewHolder"

                            override fun getConstraints(): Matcher<View>? = null

                            override fun perform(uiController: UiController?, view: View) {
                                Assert.assertEquals(cartListData?.shopGroupAvailableDataList?.get(shopIndex)?.cartItemDataList?.get(i)?.cartItemData?.originData?.productName, view.findViewById<Typography>(com.tokopedia.cart.R.id.text_product_name).text.toString())
                            }
                        }))

            } catch (e: PerformException) {
                e.printStackTrace()
            }
        }
        childRecyclerView.contentDescription = tempStoreDesc
    }

    fun clickPromoButton() {
        onView(withId(com.tokopedia.cart.R.id.promo_checkout_btn_cart)).perform(ViewActions.click())
    }

    fun clickBuyButton() {
        onView(withId(com.tokopedia.cart.R.id.go_to_courier_page_button)).perform(ViewActions.click())
    }

    infix fun validateAnalytics(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }

}