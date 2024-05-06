package com.tokopedia.pdpCheckout.testing.checkout.robot

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.checkout.R
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutButtonPaymentViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutOrderViewHolder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.isA
import com.tokopedia.logisticcart.R as logisticcartR

fun checkoutPageRevamp(func: CheckoutPageRevampRobot.() -> Unit) =
    CheckoutPageRevampRobot().apply(func)

class CheckoutPageRevampRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    private fun scrollRecyclerViewToFirstOrder(): Int {
        onView(withId(R.id.rv_checkout)).perform(RecyclerViewActions.scrollToHolder(isA(CheckoutOrderViewHolder::class.java)))
        return 0
    }

    fun scrollRecyclerViewToChoosePaymentButton(): Int {
        onView(withId(R.id.rv_checkout)).perform(RecyclerViewActions.scrollToHolder(isA(CheckoutButtonPaymentViewHolder::class.java)))
        return 0
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
            ViewActions.click().perform(uiController, view.findViewById(viewId))
    }

    fun clickChooseDuration() {
        scrollRecyclerViewToFirstOrder()
            onView(withId(R.id.rv_checkout))
                .perform(
                    RecyclerViewActions.actionOnHolderItem(
                        isA(CheckoutOrderViewHolder::class.java),
                        clickOnViewChild(logisticcartR.id.layout_state_no_selected_shipping)
                    )
                )
    }

    fun selectFirstShippingDurationOption() {
        onView(ViewMatchers.withText("Bebas Ongkir")).perform(ViewActions.click())
    }

    fun clickChoosePaymentButton() {
        scrollRecyclerViewToChoosePaymentButton()
        onView(withId(R.id.rv_checkout))
            .perform(
                RecyclerViewActions.actionOnHolderItem(
                    isA(CheckoutButtonPaymentViewHolder::class.java),
                    clickOnViewChild(R.id.btn_checkout_pay)
                )
            )
    }

    infix fun validateAnalytics(func: ResultRevampRobot.() -> Unit): ResultRevampRobot {
        return ResultRevampRobot().apply(func)
    }
}

class ResultRevampRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryFileName: String) {
        assertThat(cassavaTestRule.validate(queryFileName), hasAllSuccess())
    }
}
