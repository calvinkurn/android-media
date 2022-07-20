package com.tokopedia.inactivephone_otp.testing.inactivephone.submitnewphone

import androidx.test.espresso.Espresso
import com.tokopedia.inactivephone_otp.testing.R
import com.tokopedia.inactivephone_otp.testing.common.robot.clickOnButton
import com.tokopedia.inactivephone_otp.testing.common.robot.clickOnButtonWithText

fun inactivePhoneSubmitData(
    func: InactivePhoneSubmitDataRobot.() -> Unit
) = InactivePhoneSubmitDataRobot().apply(func)

class InactivePhoneSubmitDataRobot {

    fun clickOnButtonBack() {
        Espresso.pressBack()
    }

    fun clickOnButtonSimpan() {
        clickOnButtonWithText("Simpan")
    }

    fun clickOnButtonLanjutUbah() {
        clickOnButton(R.id.dialog_btn_secondary)
    }
}