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

const val INVALID_PHONE_NUMBER = "081212121212"

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

fun isErrorFieldEmpty() {
    typingTextOn(R.id.field_input_phone, "0821")
    clearText(InputType.TYPE_CLASS_PHONE)
    isTextDisplayed( "Wajib diisi.")
}

fun isErrorFieldPhoneTooShort() {
    typingTextOn(R.id.field_input_phone, "0821")
    isErrorTooShortDisplayed()
}

fun isErrorTooShortDisplayed() {
    isTextDisplayed("Min. 9 digit.")
}

fun isErrorFieldPhoneExceedLength() {
    typingTextOn(R.id.field_input_phone, "12345678901234567890")
    isTextDisplayed("Maks. 15 angka")
}

fun inputInvalidPhoneThenShowError() {
    inputTextThenCloseKeyboard(InputType.TYPE_CLASS_PHONE, INVALID_PHONE_NUMBER)
    clickOnDisplayedView(R.id.btn_submit)
    isTextDisplayed("Nomor ponsel sudah terdaftar")
}
