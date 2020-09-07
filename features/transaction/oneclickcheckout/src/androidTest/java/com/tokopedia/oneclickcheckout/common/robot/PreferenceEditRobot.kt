package com.tokopedia.oneclickcheckout.common.robot

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.preference.edit.view.address.AddressListItemAdapter.AddressListViewHolder
import org.hamcrest.Matcher

fun addressListPage(func: AddressListRobot.() -> Unit) = AddressListRobot().apply(func)

class AddressListRobot {

    fun clickAddress(position: Int = 0) {
        onView(withId(R.id.address_list_rv)).perform(actionOnItemAtPosition<AddressListViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "perform click on address item"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.performClick()
            }
        }))
    }

    fun clickSimpan() {
        onView(withId(R.id.btn_save_address)).perform(click())
    }
}

fun shippingDurationPage(func: ShippingDurationRobot.() -> Unit) = ShippingDurationRobot().apply(func)

class ShippingDurationRobot {

    fun clickShippingDuration(position: Int = 0) {
        onView(withId(R.id.shipping_duration_rv)).perform(actionOnItemAtPosition<AddressListViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "perform click on shipping duration item"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.performClick()
            }
        }))
    }

    fun clickSimpan() {
        onView(withId(R.id.btn_save_duration)).perform(click())
    }
}