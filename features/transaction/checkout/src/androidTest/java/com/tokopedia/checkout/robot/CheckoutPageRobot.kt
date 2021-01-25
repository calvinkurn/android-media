package com.tokopedia.checkout.robot

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.checkout.R
import org.junit.Assert

fun checkoutPage(func: CheckoutPageRobot.() -> Unit) = CheckoutPageRobot().apply(func)

class CheckoutPageRobot {

    fun openDurationBottomsheet() {
        onView(ViewMatchers.withId(R.id.rv_shipment))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                        clickOnViewChild(R.id.layout_state_no_selected_shipping)))
    }

    fun selectFirstShippingDurationOption() {
        onView(ViewMatchers.withText("Bebas Ongkir")).perform(ViewActions.click())
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = ViewActions.click().perform(uiController, view.findViewById(viewId))
    }


    infix fun choosePayment(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }

}

class ResultRobot {

    fun hasPassedAnalytics(gtmLogDBSource: GtmLogDBSource, context: Context, queryFileName: String) {
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, queryFileName), hasAllSuccess())
    }

}