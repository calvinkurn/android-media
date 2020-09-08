package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat

fun addAddress(func: AddressRobot.() -> Unit) = AddressRobot().apply(func)

class AddressRobot {

    fun launchFrom(rule: ActivityTestRule<PinpointMapActivity>, screenName: String) {
        val i = Intent()
        i.putExtra(CheckoutConstant.EXTRA_REF, screenName)
        rule.launchActivity(i)
    }

    fun searchWithKeyword(keyword: String) {
        onView(withId(R.id.et_search))
                .check(matches(isDisplayed()))
                .perform (typeText(keyword), closeSoftKeyboard())
        // delay for text field debounce
        Thread.sleep(500L)
    }

    fun selectFirstItem() {
        onView(withId(R.id.rv_poi_list))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    fun addressDetail(detail: String) {
        onView(withId(R.id.et_detail_address))
                .perform(typeText(detail), closeSoftKeyboard())
        onView(withId(R.id.btn_choose_location)).perform(click())
    }

    fun receiver(receiver: String) {
        onView(withId(R.id.et_receiver_name))
                .perform(typeText(receiver), closeSoftKeyboard())
    }

    fun phoneNumber(phone: String) {
        onView(withId(R.id.et_phone)).perform(typeText(phone), closeSoftKeyboard())
    }

    infix fun submit(func: ResultRobot.() -> Unit): ResultRobot {
        onView(withId(R.id.btn_save_address)).perform(scrollTo(), click())
        return ResultRobot().apply(func)
    }

}

class ResultRobot {
    fun hasPassedAnalytics(repository: GtmLogDBSource, queryString: String) {
        assertThat(getAnalyticsWithQuery(repository, queryString), hasAllSuccess())
    }
}