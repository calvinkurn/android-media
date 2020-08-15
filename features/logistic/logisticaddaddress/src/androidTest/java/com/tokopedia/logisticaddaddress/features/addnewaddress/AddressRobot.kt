package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import org.hamcrest.MatcherAssert

fun addAddress(func: AddressRobot.() -> Unit) = AddressRobot().apply(func)

class AddressRobot {

    fun launchFrom(rule: ActivityTestRule<PinpointMapActivity>, screenName: String) {
        val i = Intent()
        i.putExtra(CheckoutConstant.EXTRA_REF, screenName)
        rule.launchActivity(i)
        // Startup activity
        delayShort()
    }

    fun searchWithKeyword(keyword: String) {
        Espresso.onView(ViewMatchers.withId(R.id.et_search)).perform(ViewActions.typeText(keyword), ViewActions.closeSoftKeyboard())
        delayShort()
    }

    fun selectFirstItem() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_poi_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))
        delayShort()
    }

    fun addressDetail(detail: String) {
        Espresso.onView(ViewMatchers.withId(R.id.et_detail_address))
                .perform(ViewActions.typeText(detail), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btn_choose_location)).perform(ViewActions.click())
    }

    fun receiver(receiver: String) {
        Espresso.onView(ViewMatchers.withId(R.id.et_receiver_name)).perform(ViewActions.typeText(receiver), ViewActions.closeSoftKeyboard())
    }

    fun phoneNumber(phone: String) {
        Espresso.onView(ViewMatchers.withId(R.id.et_phone)).perform(ViewActions.typeText("087255991177"), ViewActions.closeSoftKeyboard())
    }

    fun submit(func: ResultRobot.() -> Unit = {}): ResultRobot {
        Espresso.onView(ViewMatchers.withId(R.id.btn_save_address)).perform(ViewActions.scrollTo(), ViewActions.click())
        return ResultRobot().apply(func)
    }

    private fun delayShort() {
        Thread.sleep(1000L)
    }
}

class ResultRobot {
    fun hasPassedAnalytics(repository: GtmLogDBSource, queryString: String) {
        MatcherAssert.assertThat(getAnalyticsWithQuery(repository, queryString), hasAllSuccess())
    }
}