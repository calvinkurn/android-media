package com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.robot

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.common.topupbills.view.model.search.TopupBillsSearchNumberDataModel
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder.EmoneyPdpProductViewHolder
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not

/** Based on EmoneyPdpActivityLoginTest from recharge_pdp_emoney module */

fun emoneyPdp(func: EmoneyPDPRobot.() -> Unit) = EmoneyPDPRobot().apply(func)

class EmoneyPDPRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun clickOnFavNumberOnInputView() {
        Intents.intending(IntentMatchers.hasComponent(
            ComponentNameMatchers.hasClassName("com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity")))
            .respondWith(createOrderNumberTypeManual())
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input)).perform(click())
        Thread.sleep(2000)

        onView(withText("8768 5678 9101 2345")).check(matches(isDisplayed()))
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_icon_2)).check(matches(isDisplayed()))
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_icon_2)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.emoneyRecentNumberList)).check(matches(isDisplayed()))
        Thread.sleep(1000)

        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input)).perform(click())
        Thread.sleep(1000)
    }

    fun clickProduct() {
        onView(withId(R.id.emoneyBuyWidget)).check(matches(not(isDisplayed())))
        onView(withId(R.id.emoneyProductWidgetTitle)).check(
            matches(withText("Mau top-up berapa?")))
        onView(allOf(withId(R.id.emoneyProductTitle), withText("Rp 10.000")))
            .check(matches(isDisplayed()))
        onView(allOf(withId(R.id.emoneyProductPrice), withText("Rp10.000")))
            .check(matches(isDisplayed()))
        onView(withId(R.id.emoneyProductListRecyclerView)).check(matches(isDisplayed()))
            .perform(RecyclerViewActions.actionOnItemAtPosition<EmoneyPdpProductViewHolder>(
                0, click()
            )
        )
        Thread.sleep(1000)
        onView(withId(R.id.emoneyBuyWidget)).check(matches(isDisplayed()))
        onView(withId(R.id.emoneyPdpCheckoutViewTotalPayment)).check(matches(isDisplayed()))
        onView(withId(R.id.emoneyPdpCheckoutViewTotalPayment)).check(matches(withText("Rp10.000")))
    }

    fun clickBuy() {
        onView(withId(R.id.emoneyPdpCheckoutViewButton)).perform(click())
    }

    private fun createOrderNumberTypeManual(): Instrumentation.ActivityResult {
        val orderClientNumber = TopupBillsSearchNumberDataModel(clientNumber = "8768567891012345")
        val resultData = Intent()
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber)
        resultData.putExtra(
            TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE,
            TopupBillsSearchNumberFragment.InputNumberActionType.FAVORITE)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }
}
