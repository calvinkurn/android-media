package com.tokopedia.profilecompletion.addphone

import android.text.InputType
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.common.helper.clearText
import com.tokopedia.profilecompletion.common.helper.clickOnDisplayedView
import com.tokopedia.profilecompletion.common.helper.inputTextThenCloseKeyboard
import com.tokopedia.profilecompletion.common.helper.isDisplayed
import com.tokopedia.profilecompletion.common.helper.isEnable
import com.tokopedia.profilecompletion.common.helper.isTextDisplayed
import com.tokopedia.profilecompletion.common.helper.typingTextOn

const val INVALID_VALIDATE_PHONE_NUMBER = "081212121212"
const val INVALID_UPDATE_PHONE_NUMBER = "081313131313"
const val VALID_UPDATE_PHONE_NUMBER = "081414141414"

fun isInitViewDisplayed() {
    isDisplayed(
        R.id.iv_input_phone,
        R.id.tv_input_phone_header,
        R.id.tv_input_phone_desc,
        R.id.field_input_phone,
        R.id.btn_submit
    )
    isEnable(false, R.id.btn_submit)
}

fun isViewFailedOtpDisplayed() {
    isDisplayed(
        R.id.iv_input_phone,
        R.id.tv_input_phone_header,
        R.id.tv_input_phone_desc,
        R.id.field_input_phone,
        R.id.btn_submit
    )
    isEnable(true, R.id.btn_submit)
}

fun isErrorFieldEmpty() {
    inputTextThenCloseKeyboard(InputType.TYPE_CLASS_PHONE, "0821")
    clearText(InputType.TYPE_CLASS_PHONE)
    isTextDisplayed( "Wajib diisi.")
}

fun isErrorFieldPhoneTooShort() {
    inputTextThenCloseKeyboard(InputType.TYPE_CLASS_PHONE, "0821")
    isErrorTooShortDisplayed()
}

fun isErrorTooShortDisplayed() {
    isTextDisplayed("Min. 9 digit.")
}

fun isErrorFieldPhoneExceedLength() {
    inputTextThenCloseKeyboard(InputType.TYPE_CLASS_PHONE, "12345678901234567890")
    isTextDisplayed("Maks. 15 angka")
}

fun inputInvalidValidatePhoneThenShowError() {
    inputTextThenCloseKeyboard(InputType.TYPE_CLASS_PHONE, INVALID_VALIDATE_PHONE_NUMBER)
    clickOnDisplayedView(R.id.btn_submit)
    isTextDisplayed("Nomor ponsel sudah terdaftar")
}

fun inputInvalidUpdatePhoneThenShowError() {
    inputTextThenCloseKeyboard(InputType.TYPE_CLASS_PHONE, INVALID_UPDATE_PHONE_NUMBER)
    clickOnDisplayedView(R.id.btn_submit)
    isDisplayed(R.id.global_error)
}

fun inputValidUpdatePhoneThenSuccess() {
    inputTextThenCloseKeyboard(InputType.TYPE_CLASS_PHONE, VALID_UPDATE_PHONE_NUMBER)
    clickOnDisplayedView(R.id.btn_submit)
}
