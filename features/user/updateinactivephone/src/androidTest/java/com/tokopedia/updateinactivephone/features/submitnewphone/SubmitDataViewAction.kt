package com.tokopedia.updateinactivephone.features.submitnewphone

import com.tokopedia.updateinactivephone.common.viewaction.clickOnButton
import com.tokopedia.updateinactivephone.common.viewaction.isDisplayed
import com.tokopedia.updateinactivephone.common.viewaction.scrollToView
import com.tokopedia.updateinactivephone.common.viewaction.setText
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
}