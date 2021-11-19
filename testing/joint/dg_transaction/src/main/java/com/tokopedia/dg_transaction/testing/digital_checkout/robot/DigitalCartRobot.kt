package com.tokopedia.dg_transaction.testing.digital_checkout.robot

import android.app.Instrumentation
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
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(
            PAYMENT_FAILED, null))
        onView(withId(R.id.btnCheckout)).perform(click())
    }

    companion object {
        /** source: com.tokopedia.common.payment.PaymentConstant */
        const val PAYMENT_FAILED = 7
    }
}