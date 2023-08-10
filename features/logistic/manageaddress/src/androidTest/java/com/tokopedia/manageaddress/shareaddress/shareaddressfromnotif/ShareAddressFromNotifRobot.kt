package com.tokopedia.manageaddress.shareaddress.shareaddressfromnotif

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
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

    fun launchFrom(rule: ActivityTestRule<ManageAddressActivity>, paramRuid: String) {
        val i = Intent()
        i.putExtra(ManageAddressConstant.QUERY_PARAM_RUID, paramRuid)
        rule.launchActivity(i)
        waitForData()
    }

    fun selectFirstAddress() {
        Espresso.onView(
            RecyclerViewMatcher(R.id.address_list)
                .atPositionOnView(0, R.id.card_address)
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
        waitForData()
    }

    fun clickShareAddressButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_choose_address))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
        waitForData()
    }

    fun clickDisagreeButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_disagree))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
    }

    fun clickAgreeButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_agree))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
    }

    fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun waitForData(millis: Long = 500L) {
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
