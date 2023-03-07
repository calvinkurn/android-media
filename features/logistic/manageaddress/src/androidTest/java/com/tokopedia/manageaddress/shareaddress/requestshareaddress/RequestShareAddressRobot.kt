package com.tokopedia.manageaddress.shareaddress.requestshareaddress

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.MatcherAssert.assertThat

fun shareAddress(func: ShareAddressRobot.() -> Unit) = ShareAddressRobot().apply(func)

class ShareAddressRobot {

    fun launchFrom(rule: ActivityTestRule<ManageAddressActivity>, paramSuid: String) {
        val i = Intent()
        i.putExtra(ManageAddressConstant.QUERY_PARAM_SUID, paramSuid)
        rule.launchActivity(i)
        waitForData()
    }

    fun selectFirstAddress() {
        onView(
            RecyclerViewMatcher(R.id.rv_address_list)
                .atPositionOnView(0, R.id.cb_select_address)
        ).check(matches(isDisplayed()))
            .perform(click())
    }

    fun deleteSelectedItem() {
        onView(ViewMatchers.withId(R.id.btn_delete)).perform(click())
        waitForData(4000L)
    }

    fun onClickRequestAddress() {
        onView(ViewMatchers.withId(R.id.card_request_address)).perform(click())
        waitForData()
    }

    fun clickRequestButton() {
        onView(ViewMatchers.withId(R.id.btn_share))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    fun selectAllAddress() {
        onView(ViewMatchers.withId(R.id.cb_all_address)).perform(click())
    }

    fun saveAddress() {
        onView(ViewMatchers.withId(R.id.btn_save)).perform(click())
    }

    fun clickIconContactPhoneNumber() {
        stubIntent()
        onView(ViewMatchers.withId(R.id.text_field_icon_2))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun stubIntent() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun waitForData(millis: Long = 1000L) {
        Thread.sleep(millis)
    }

    infix fun validateAnalytics(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }
}

class ResultRobot {
    fun hasPassedAnalytics(cassavaTestRule: CassavaTestRule, queryId: String) {
        assertThat(cassavaTestRule.validate(queryId), hasAllSuccess())
    }
}
