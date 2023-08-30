package com.tokopedia.inactivephone_otp.testing.inactivephone.onboarding

import androidx.test.espresso.Espresso
import com.tokopedia.inactivephone_otp.testing.R
import com.tokopedia.inactivephone_otp.testing.common.robot.clickOnButton
import com.tokopedia.inactivephone_otp.testing.common.robot.scrollToView

fun inactivePhoneOnboarding(
    func: InactivePhoneOnboardingRobot.() -> Unit
) = InactivePhoneOnboardingRobot().apply(func)

class InactivePhoneOnboardingRobot {

    fun clickOnButtonBack() {
        Espresso.pressBack()
    }

    fun clickOnButtonLanjut() {
        scrollToView(com.tokopedia.updateinactivephone.R.id.button_next)
        clickOnButton(com.tokopedia.updateinactivephone.R.id.button_next)
    }
}