package com.tokopedia.pdpCheckout.testing.checkout.robot

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.checkout.R
import com.tokopedia.checkout.old.view.viewholder.PromoCheckoutViewHolder
import com.tokopedia.checkout.old.view.viewholder.ShipmentButtonPaymentViewHolder
import com.tokopedia.checkout.old.view.viewholder.ShipmentItemViewHolder
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.isA
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

fun checkoutPage(func: CheckoutPageRobot.() -> Unit) = CheckoutPageRobot().apply(func)

class CheckoutPageRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    private fun scrollRecyclerViewToFirstOrder() {
        onView(withId(R.id.rv_shipment)).perform(RecyclerViewActions.scrollToHolder(isA(ShipmentItemViewHolder::class.java)))
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

    fun clickChooseDuration() {
        onView(withId(R.id.rv_shipment))
                .perform(RecyclerViewActions.actionOnHolderItem(isA(ShipmentItemViewHolder::class.java),
                        clickOnViewChild(R.id.layout_state_no_selected_shipping)))
    }

    fun selectFirstShippingDurationOption() {
        onView(ViewMatchers.withText("Bebas Ongkir")).perform(ViewActions.click())
    }

    fun clickPromoButton() {
        onView(withId(R.id.rv_shipment))
                .perform(RecyclerViewActions.actionOnHolderItem(isA(PromoCheckoutViewHolder::class.java),
                        clickOnViewChild(R.id.promo_checkout_btn_shipment)))
    }

    fun clickChoosePaymentButton() {
        onView(withId(R.id.rv_shipment))
                .perform(RecyclerViewActions.actionOnHolderItem(isA(ShipmentButtonPaymentViewHolder::class.java),
                        clickOnViewChild(R.id.btn_select_payment_method)))
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
    fun assertHasSingleShipmentSelected(title: String, originalPrice: String? = null,
                                                       discountedPrice: String? = null, eta: String, message: String? = null) {
        scrollRecyclerViewToFirstOrder()
        onView(withId(R.id.rv_shipment))
                .perform(RecyclerViewActions.actionOnHolderItem(isA(ShipmentItemViewHolder::class.java), object : ViewAction {
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

class ResultRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryId: String) {
        assertThat(cassavaTestRule.validate(queryId), hasAllSuccess())
    }

    fun assertGoToPayment() {
        val paymentPassData = Intents.getIntents().last().getParcelableExtra<PaymentPassData>(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)
        assertNotNull(paymentPassData)
    }
}