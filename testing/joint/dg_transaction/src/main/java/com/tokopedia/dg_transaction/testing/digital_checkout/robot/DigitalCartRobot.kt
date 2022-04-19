package com.tokopedia.dg_transaction.testing.digital_checkout.robot

import android.app.Instrumentation
import android.webkit.WebView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.dg_transaction.testing.R

fun digitalCheckout(func: DigitalCartRobot.() -> Unit) = DigitalCartRobot().apply(func)

class DigitalCartRobot {

    fun waitForData() {
        Thread.sleep(3000)
    }

    fun clickCheckout() {
        /** Change this intending to specific class later when you want to implement thankyou page */
        onView(withId(R.id.btnCheckout)).perform(click())
    }
}