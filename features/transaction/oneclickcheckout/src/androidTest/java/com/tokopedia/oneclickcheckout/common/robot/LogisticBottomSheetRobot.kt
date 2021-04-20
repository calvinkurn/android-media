package com.tokopedia.oneclickcheckout.common.robot

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.preference.edit.view.address.AddressListItemViewHolder
import org.hamcrest.Matcher

class AddressBottomSheetRobot {

    fun clickAddress(position: Int = 0) {
        onView(withId(R.id.rv_address_list)).perform(actionOnItemAtPosition<AddressListItemViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "perform click on address item"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.performClick()
            }
        }))
    }
}

class CourierBottomSheetRobot {

    fun chooseCourierWithText(text: String) {
        onView(withText(text)).perform(click())
    }
}

class DurationBottomSheetRobot {

    fun chooseDurationWithText(text: String) {
        onView(withText(text)).perform(click())
    }
}