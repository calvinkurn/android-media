package com.tokopedia.inactivephone_otp.testing.otp.verification

import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.inactivephone_otp.testing.common.robot.UnifyComponent
import com.tokopedia.inactivephone_otp.testing.common.robot.clickClickableSpan
import com.tokopedia.inactivephone_otp.testing.common.robot.waitOnView
import com.tokopedia.otp.R
import org.hamcrest.CoreMatchers

fun otpVerification(func: OtpVerificationRobot.() -> Unit) = OtpVerificationRobot().apply(func)

class OtpVerificationRobot {

    fun clickOnUbahNomorHp() {
        waitOnView(
            ViewMatchers.withText(CoreMatchers.containsString("Tidak menerima kode?")),
            35000,
            500
        )
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(clickClickableSpan("Ajukan perubahan nomor HP"))
    }

    fun fillOtpWithCode(code: String) {
        waitOnView(withId(R.id.pin))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(UnifyComponent.fillPinUnify(code))
    }
}