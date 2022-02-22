package com.tokopedia.inactivephone_otp.testing.otp.modelist

import com.tokopedia.inactivephone_otp.testing.R
import com.tokopedia.inactivephone_otp.testing.common.robot.*

fun otpModeList(func: OtpModeListRobot.() -> Unit) = OtpModeListRobot().apply(func)

class OtpModeListRobot {

    fun clickOnUbahNomorHp() {
        scrollOnNestedView(R.id.phone_inactive)
        clickOnButton(R.id.phone_inactive)
    }

    fun clickOnOtpModeEmail() {
        clickOnButtonWithText("E-mail ke rivaldy.firmansyah+130@tokopedia.com")
    }
}