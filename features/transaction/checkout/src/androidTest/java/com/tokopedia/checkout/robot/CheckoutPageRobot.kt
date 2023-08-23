package com.tokopedia.checkout.robot

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
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.checkout.R
import com.tokopedia.checkout.ShipmentActivity
import com.tokopedia.checkout.view.viewholder.PromoCheckoutViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentButtonPaymentViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemBottomViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemViewHolder
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

fun checkoutPage(func: CheckoutPageRobot.() -> Unit) = CheckoutPageRobot().apply(func)

class CheckoutPageRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    private fun scrollRecyclerViewToFirstShipmentCartItemBottom(activityRule: IntentsTestRule<ShipmentActivity>): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_shipment)
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        var position = RecyclerView.NO_POSITION
        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(activityRule, recyclerView, i)
            when (recyclerView.findViewHolderForAdapterPosition(i)) {
                is ShipmentCartItemBottomViewHolder -> {
                    position = i
                    break
                }
            }
        }

        return position
    }

    private fun scrollRecyclerViewToShipmentCartItem(activityRule: IntentsTestRule<ShipmentActivity>, productIndex: Int): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_shipment)
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        var position = RecyclerView.NO_POSITION
        var currentIndex = 0
        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(activityRule, recyclerView, i)
            when (recyclerView.findViewHolderForAdapterPosition(i)) {
                is ShipmentCartItemViewHolder -> {
                    position = i
                    if (currentIndex == productIndex) break else currentIndex += 1
                }
            }
        }

        return position
    }

    fun scrollRecyclerViewToChoosePaymentButton(activityRule: IntentsTestRule<ShipmentActivity>): Int {
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

    private fun scrollRecyclerViewToPromoButton(activityRule: IntentsTestRule<ShipmentActivity>): Int {
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

    private fun scrollRecyclerViewToPosition(
        activityRule: IntentsTestRule<ShipmentActivity>,
        recyclerView: RecyclerView,
        position: Int
    ) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = ViewActions.click().perform(uiController, view.findViewById(viewId))
    }

    fun clickChooseDuration(activityRule: IntentsTestRule<ShipmentActivity>) {
        val position = scrollRecyclerViewToFirstShipmentCartItemBottom(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_shipment))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        clickOnViewChild(com.tokopedia.logisticcart.R.id.layout_state_no_selected_shipping)
                    )
                )
        }
    }

    fun selectFirstShippingDurationOption() {
        onView(ViewMatchers.withText("Bebas Ongkir")).perform(ViewActions.click())
    }

    fun clickPromoButton(activityRule: IntentsTestRule<ShipmentActivity>) {
        val position = scrollRecyclerViewToPromoButton(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_shipment))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        clickOnViewChild(R.id.promo_checkout_btn_shipment)
                    )
                )
        }
    }

    fun clickChoosePaymentButton(activityRule: IntentsTestRule<ShipmentActivity>) {
        val position = scrollRecyclerViewToChoosePaymentButton(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_shipment))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        clickOnViewChild(R.id.btn_select_payment_method)
                    )
                )
        }
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
    fun assertHasSingleShipmentSelected(
        activityRule: IntentsTestRule<ShipmentActivity>,
        title: String,
        originalPrice: String? = null,
        discountedPrice: String? = null,
        eta: String,
        message: String? = null
    ) {
        val position = scrollRecyclerViewToFirstShipmentCartItemBottom(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_shipment))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String = "Assert Single Shipment Selected UI"

                            override fun perform(uiController: UiController?, view: View) {
                                assertEquals(View.VISIBLE, view.findViewById<View>(com.tokopedia.logisticcart.R.id.layout_state_has_selected_single_shipping).visibility)
                                assertEquals(title, view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.label_selected_single_shipping_title).text.toString())
                                if (originalPrice != null) {
                                    Assert.assertTrue((view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.label_selected_single_shipping_title).text).contains(originalPrice))
                                }
                                if (discountedPrice != null) {
                                    Assert.assertTrue((view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.label_selected_single_shipping_title).text).contains(discountedPrice))
                                }
                                assertEquals(eta, view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.label_single_shipping_eta).text)
                                if (message != null) {
                                    assertEquals(message, view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.label_single_shipping_message).text.toString())
                                    assertEquals(View.VISIBLE, view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.label_single_shipping_message).visibility)
                                } else {
                                    assertEquals(View.GONE, view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.label_single_shipping_message).visibility)
                                }
                            }
                        }
                    )
                )
        }
    }

    fun assertNewUiGroupType(activityRule: IntentsTestRule<ShipmentActivity>, productIndex: Int) {
        val position = scrollRecyclerViewToShipmentCartItem(activityRule, productIndex)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_shipment))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        object : ViewAction {
                            override fun getConstraints(): Matcher<View>? = null

                            override fun getDescription(): String = "Assert New UI Group Type"

                            override fun perform(uiController: UiController?, view: View) {
                                assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.tv_shop_name).visibility)
                                assertEquals(true, view.findViewById<Typography>(R.id.tv_shop_name).text.isNotBlank())
                            }
                        }
                    )
                )
        }
    }
}

class ResultRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
        assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
    }

    fun assertGoToPayment() {
        val paymentPassData = Intents.getIntents().last().getParcelableExtra<PaymentPassData>(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)
        assertNotNull(paymentPassData)
    }
}
