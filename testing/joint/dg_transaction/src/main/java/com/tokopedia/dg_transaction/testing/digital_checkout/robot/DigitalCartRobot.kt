package com.tokopedia.dg_transaction.testing.digital_checkout.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.dg_transaction.testing.R

fun digitalCheckout(func: DigitalCartRobot.() -> Unit) = DigitalCartRobot().apply(func)

class DigitalCartRobot {

    fun waitForData() {
        Thread.sleep(6000)
    }

    fun clickCheckout() {
        onView(withId(R.id.btnCheckout)).perform(click())
    }
}