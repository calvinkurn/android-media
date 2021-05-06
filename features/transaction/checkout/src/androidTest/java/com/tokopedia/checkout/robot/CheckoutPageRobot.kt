package com.tokopedia.checkout.robot

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentActivity
import com.tokopedia.checkout.view.viewholder.PromoCheckoutViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentButtonPaymentViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentItemViewHolder
import org.hamcrest.core.AllOf
import org.junit.Assert

fun checkoutPage(func: CheckoutPageRobot.() -> Unit) = CheckoutPageRobot().apply(func)

class CheckoutPageRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    private fun scrollRecyclerViewToFirstOrder(activityRule: IntentsTestRule<ShipmentActivity>): Int {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_shipment)
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        var position = RecyclerView.NO_POSITION
        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(activityRule, recyclerView, i)
            when (recyclerView.findViewHolderForAdapterPosition(i)) {
                is ShipmentItemViewHolder -> {
                    position = i
                    break
                }
            }
        }

        return position
    }

    private fun scrollRecyclerViewToChoosePaymentButton(activityRule: IntentsTestRule<ShipmentActivity>): Int {
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

    private fun scrollRecyclerViewToPosition(activityRule: IntentsTestRule<ShipmentActivity>,
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

    fun clickChooseDuration(activityRule: IntentsTestRule<ShipmentActivity>) {
        val position = scrollRecyclerViewToFirstOrder(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_shipment))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position,
                            clickOnViewChild(R.id.layout_state_no_selected_shipping)))
        }
    }

    fun selectFirstShippingDurationOption() {
        onView(ViewMatchers.withText("Bebas Ongkir")).perform(ViewActions.click())
    }

    fun clickPromoButton(activityRule: IntentsTestRule<ShipmentActivity>) {
        val position = scrollRecyclerViewToPromoButton(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_shipment))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position,
                            clickOnViewChild(R.id.promo_checkout_btn_shipment)))
        }
    }

    fun clickChoosePaymentButton(activityRule: IntentsTestRule<ShipmentActivity>) {
        val position = scrollRecyclerViewToChoosePaymentButton(activityRule)
        if (position != RecyclerView.NO_POSITION) {
            onView(ViewMatchers.withId(R.id.rv_shipment))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position,
                            clickOnViewChild(R.id.btn_select_payment_method)))
        }
    }

    infix fun validateAnalytics(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }

}

class ResultRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun hasPassedAnalytics(gtmLogDBSource: GtmLogDBSource, context: Context, queryFileName: String) {
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, queryFileName), hasAllSuccess())
    }

}