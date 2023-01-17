package com.tokopedia.loginregister.redefineregisteremail.view.inputphone

import android.text.InputType
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.redefineregisteremail.stub.common.clearText
import com.tokopedia.loginregister.redefineregisteremail.stub.common.clickOnButtonDialog
import com.tokopedia.loginregister.redefineregisteremail.stub.common.clickOnText
import com.tokopedia.loginregister.redefineregisteremail.stub.common.clickOnView
import com.tokopedia.loginregister.redefineregisteremail.stub.common.inputText
import com.tokopedia.loginregister.redefineregisteremail.stub.common.isDisplayed
import com.tokopedia.loginregister.redefineregisteremail.stub.common.isEnable
import com.tokopedia.loginregister.redefineregisteremail.stub.common.isInputTypeEnable
import com.tokopedia.loginregister.redefineregisteremail.stub.common.isTextDisplayed

private const val inputTypePhone = InputType.TYPE_CLASS_PHONE
private const val validPhoneNumber = "081234567890"

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

fun inputValidPhone() {
    inputText(inputTypePhone, validPhoneNumber)
}

fun isUserProfileValidateMessageDisplayed() {
    isTextDisplayed("Nomor telepon anda sudah terdaftar!")
}

fun isAllViewEnabled() {
    isInputTypeEnable(true, inputTypePhone)
    isEnable(true, R.id.btn_submit)
}

fun isErrorFieldEmpty() {
    inputText(inputTypePhone, "0821")
    clearText(inputTypePhone)
    isTextDisplayed("Harus diisi!")
}

fun isErrorFieldPhoneTooShort() {
    inputText(inputTypePhone, "0821")
    isErrorTooShortDisplayed()
}

fun isErrorFieldPhoneExceedLength() {
    val inputType = inputTypePhone
    inputText(inputType, "12345678901234567890")
    isTextDisplayed("Maks. 15 angka")
}

fun clickSubmit() {
    clickOnView(R.id.btn_submit)
}

fun clickLewati() {
    clickOnText("Lewati")
}

fun isErrorTooShortDisplayed() {
    isTextDisplayed("Min. 8 angka")
}

fun cantNavigateBack() {
    isTextDisplayed("Lewati")
}

fun isDialogOfferLoginShowing() {
    isTextDisplayed(
        "Nomor HP Sudah Terdaftar",
        "Lanjut masuk dengan nomor ini\n$validPhoneNumber"
    )
    isDisplayed(
        R.id.dialog_btn_secondary,
        R.id.dialog_btn_primary
    )
}

fun isRegisterCheckFailedMessageDisplayed() {
    isTextDisplayed("Nomor handphone sudah terdaftar.")
}

fun isDialogConfirmPhoneNumberShowing() {
    isTextDisplayed(
        "Pastikan nomor HP yang kamu isi sudah benar untuk diverifikasi.",
        validPhoneNumber
    )
    isDisplayed(
        R.id.dialog_btn_secondary,
        R.id.dialog_btn_primary
    )
}

fun isGlobalErrorShowing() {
    isDisplayed(R.id.global_error)
}

fun clickPrimaryButtonDialog() {
    clickOnButtonDialog(R.id.dialog_btn_primary)
}

fun clickSecondaryButtonDialog() {
    clickOnButtonDialog(R.id.dialog_btn_secondary)
}
