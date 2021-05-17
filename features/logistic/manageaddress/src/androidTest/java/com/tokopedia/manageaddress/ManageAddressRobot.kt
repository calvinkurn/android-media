package com.tokopedia.manageaddress

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultCode
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultData
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import org.hamcrest.MatcherAssert.assertThat

class ManageAddressRobot {

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
}

fun manageAddress(func: ManageAddressRobot.() -> Unit) = ManageAddressRobot().apply(func)