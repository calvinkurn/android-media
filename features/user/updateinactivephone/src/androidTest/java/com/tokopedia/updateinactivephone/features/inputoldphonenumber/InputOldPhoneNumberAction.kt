package com.tokopedia.updateinactivephone.features.inputoldphonenumber

import com.tokopedia.updateinactivephone.common.viewaction.*
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberGeneralTest.Companion.ERROR_PHONE_EMPTY
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberGeneralTest.Companion.ERROR_PHONE_TOO_LONG
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberGeneralTest.Companion.ERROR_PHONE_TOO_SHORT
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberNotRegisteredTest.Companion.ERROR_PHONE_NOT_REGISTERED
import com.tokopedia.updateinactivephone.test.R

object InputOldPhoneNumberAction {

    fun checkInitViewIsShowing() {
        scrollAndIsDisplayed(R.id.typography_title_name)
        scrollAndIsDisplayed(R.id.text_field_input)
        scrollAndIsDisplayed(R.id.ub_next)
    }

    fun setPhoneNumberText(phoneNumber: String) {
        R.id.text_field_input.apply {
            scrollToView(this)
            setText(this, phoneNumber)
        }
    }

    fun clickOnButtonSubmit() {
        R.id.ub_next.apply {
            scrollToView(this)
            clickOnButton(this)
        }
    }

    fun checkErrorMessageOnInputPhone(errorMessage: String) {
        scrollToView(R.id.text_input_old_phone_number)
        isTextDisplayed(errorMessage)
    }

    fun checkErrorMessageIsNotDisplayed() {
        isTextNotDisplayed(ERROR_PHONE_EMPTY)
        isTextNotDisplayed(ERROR_PHONE_TOO_SHORT)
        isTextNotDisplayed(ERROR_PHONE_TOO_LONG)
        isTextNotDisplayed(ERROR_PHONE_NOT_REGISTERED)
    }

}