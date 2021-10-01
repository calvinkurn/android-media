package com.tokopedia.updateinactivephone.features.submitnewphone

import com.tokopedia.updateinactivephone.common.viewaction.*
import com.tokopedia.updateinactivephone.test.R

object SubmitDataViewAction {

    fun checkSubmitDataPageDisplayed() {
        scrollToView(R.id.txtInputPhoneNumber)
        isDisplayed(R.id.txtInputPhoneNumber)
        scrollToView(R.id.button_next)
        isDisplayed(R.id.button_next)
    }

    fun setPhoneNumberText(phone: String) {
        scrollToView(R.id.txtInputPhoneNumber)
        isDisplayed(R.id.txtInputPhoneNumber)
        setText(R.id.textPhoneNumberView, phone)
    }

    fun clickOnButtonSubmit() {
        scrollToView(R.id.button_next)
        clickOnButton(R.id.button_next)
    }

    fun checkPopupIsDisplayed() {
        isTextDisplayed("Keluar dari halaman ini?")
    }

    fun clickOnButtonExitPopup() {
        clickOnButtonWithText("Keluar")
    }

    fun clickOnButtonLanjutVerifikasi() {
        clickOnButtonWithText("Lanjut Verifikasi")
    }
}