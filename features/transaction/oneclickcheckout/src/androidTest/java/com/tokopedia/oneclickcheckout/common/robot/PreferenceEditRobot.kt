package com.tokopedia.oneclickcheckout.common.robot

import android.app.Activity
import android.view.View
import android.webkit.WebView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.address.AddressListItemViewHolder
import org.hamcrest.Matcher

fun addressListPage(func: AddressListRobot.() -> Unit) = AddressListRobot().apply(func)

class AddressListRobot {

    fun clickAddress(position: Int = 0) {
        onView(withId(R.id.address_list_rv)).perform(actionOnItemAtPosition<AddressListItemViewHolder>(position, object : ViewAction {
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

    fun clickBack() {
        onView(withId(R.id.btn_back)).perform(click())
    }

    fun clickAddButton() {
        onView(withId(R.id.btn_add)).perform(click())
    }
}

fun shippingDurationPage(func: ShippingDurationRobot.() -> Unit) = ShippingDurationRobot().apply(func)

class ShippingDurationRobot {

    fun clickShippingDuration(position: Int = 0) {
        onView(withId(R.id.shipping_duration_rv)).perform(actionOnItemAtPosition<AddressListItemViewHolder>(position, object : ViewAction {
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

    fun clickBack() {
        onView(withId(R.id.btn_back)).perform(click())
    }
}

fun paymentMethodPage(func: PaymentMethodRobot.() -> Unit) = PaymentMethodRobot().apply(func)

class PaymentMethodRobot {

    fun pretendChoosePayment(activity: Activity) {
        activity.runOnUiThread {
            activity.findViewById<WebView>(R.id.web_view).loadUrl("https://link.link/link?success=true&gateway_code=gateway1")
        }
        //block main thread for webview processing
        Thread.sleep(1000)
    }

    fun clickBack() {
        onView(withId(R.id.btn_back)).perform(click())
    }
}

fun preferenceSummaryPage(func: PreferenceSummaryRobot.() -> Unit) = PreferenceSummaryRobot().apply(func)

class PreferenceSummaryRobot {

    fun clickBack() {
        onView(withId(R.id.btn_back)).perform(click())
    }

    fun clickEditAddress() {
        onView(withId(R.id.btn_change_address)).perform(click())
    }

    fun clickEditShipping() {
        onView(withId(R.id.btn_change_duration)).perform(click())
    }

    fun clickEditPayment() {
        onView(withId(R.id.btn_change_payment)).perform(click())
    }

    fun clickSave() {
        onView(withId(R.id.btn_save)).perform(click())
    }
}