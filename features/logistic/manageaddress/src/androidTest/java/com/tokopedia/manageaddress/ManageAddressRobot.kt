package com.tokopedia.manageaddress

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultCode
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultData
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.tokopedia.manageaddress.CustomMatchers.isCardUnifyChecked
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat

class ManageAddressRobot {

    fun launchWithParam(activityRule: ActivityTestRule<ManageAddressActivity>, intent: Intent) {
        activityRule.launchActivity(intent)
    }

    fun selectItemAt(position: Int) {
        onView(withId(R.id.address_list))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
    }

    fun clickShareIconOnPosition(position: Int) {
        onView(RecyclerViewMatcher(R.id.address_list).atPositionOnView(position, R.id.icon_share))
            .perform(click())
    }

    fun typeEmailThenSubmit(s: String) {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input))
            .perform(typeText(s), closeSoftKeyboard())

        onView(withId(R.id.btn_share))
            .perform(click())
    }

    fun clickAgreeButton() {
        onView(withId(R.id.btn_agree))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    fun selectAddress() {
        onView(withId(R.id.btn_choose_address)).perform(click())
    }

    fun onClickSearch(keyword: String) {
        onView(withId(R.id.search_input_view)).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click(), typeText(keyword), closeSoftKeyboard())
        waitForData()
    }

    private fun waitForData(millis: Long = 1000L) {
        Thread.sleep(millis)
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

    fun hasDisplayedText(s: String) {
        onView(withText(containsString(s))).check(matches(isDisplayed()))
    }

    fun hasDisplayedText(sId: Int) {
        onView(withText(sId)).check(matches(isDisplayed()))
    }

    fun assertGlobalErrorNoInternetConnectionShown() {
        onView(allOf(withId(R.id.global_error), isCompletelyDisplayed()))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(firstView(withText("Koneksi internetmu terganggu!"))).check(matches(isDisplayed()))
    }

    fun assertAddressCheckedAtPosition(position: Int) {
        onView(RecyclerViewMatcher(R.id.address_list).atPositionOnView(position, R.id.card_address))
            .check(matches(isCardUnifyChecked()))
    }
}

fun manageAddress(func: ManageAddressRobot.() -> Unit) = ManageAddressRobot().apply(func)
