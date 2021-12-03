package com.tokopedia.dg_transaction.testing.thankyou_native.robot

import android.app.Activity
import com.tokopedia.applink.RouteManager
import com.tokopedia.dg_transaction.testing.digital_checkout.robot.DigitalCartRobot

fun thankYou(func: ThankYouRobot.() -> Unit) = ThankYouRobot().apply(func)

class ThankYouRobot {

    fun waitForData() {
        Thread.sleep(3000)
    }

    fun mockPayAndNavigateThankYou(activity: Activity) {
        RouteManager.route(activity, "tokopedia://payment/thankyou?payment_id=853304807&merchant=tokopedia")
    }
}