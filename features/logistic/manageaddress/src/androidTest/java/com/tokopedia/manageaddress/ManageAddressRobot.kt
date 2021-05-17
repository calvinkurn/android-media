package com.tokopedia.manageaddress

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultCode
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultData
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import org.hamcrest.MatcherAssert.assertThat

class ManageAddressRobot {

    fun launchWithParam(activityRule: ActivityTestRule<ManageAddressActivity>, intent: Intent) {
        activityRule.launchActivity(intent)
    }

    fun selectItemAt(position: Int) {
        onView(withId(R.id.address_list))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, ViewActions.click()))
    }

    fun selectAddress() {
        onView(withId(R.id.btn_choose_address)).perform(click())
    }

    infix fun submit(func: ResultRobot.() -> Unit) = ResultRobot().apply(func)
}

class ResultRobot {
    fun hasResultIntent(activityRule: ActivityTestRule<ManageAddressActivity>, code: Int, dataKey: String? = null) {
        assertThat(activityRule.activityResult, hasResultCode(code))
        dataKey?.let {
            assertThat(activityRule.activityResult, hasResultData(hasExtraWithKey(it)))
        }
    }

    fun globalErrorNoInternetConnectionShown() {
        onView(withId(R.id.global_error)).check(matches(isDisplayed()))
        onView(withText("Koneksi internetmu terganggu!")).check(matches(isDisplayed()))
    }
}

fun manageAddress(func: ManageAddressRobot.() -> Unit) = ManageAddressRobot().apply(func)