package com.tokopedia.loginregister.redefineregisteremail.view.inputemail

import android.text.InputType
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.redefineregisteremail.stub.common.clearText
import com.tokopedia.loginregister.redefineregisteremail.stub.common.clickOnView
import com.tokopedia.loginregister.redefineregisteremail.stub.common.inputText
import com.tokopedia.loginregister.redefineregisteremail.stub.common.isDisplayed
import com.tokopedia.loginregister.redefineregisteremail.stub.common.isEnable
import com.tokopedia.loginregister.redefineregisteremail.stub.common.isFieldEnable
import com.tokopedia.loginregister.redefineregisteremail.stub.common.isTextDisplayed

private const val inputTypeEmail = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
private const val inputTypePassword =
    InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
private const val inputTypeName = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS

private const val validEmail = "habibi@tokopedia.com"

fun isInitViewDisplayed() {
    isDisplayed(
        R.id.field_email,
        R.id.field_password,
        R.id.field_name,
        R.id.btn_submit,
        R.id.tv_desc
    )
    isEnable(false, R.id.btn_submit)
}

fun isButtonSubmitEnabled() {
    isEnable(true, R.id.btn_submit)
}

fun isAllFieldEnabled() {
    isFieldEnable(true, inputTypeEmail)
    isFieldEnable(true, inputTypePassword)
    isFieldEnable(true, inputTypeName)
}

fun clickSubmit() {
    clickOnView(R.id.btn_submit)
}

fun isAllFieldErrorDisplayed() {
    isTextDisplayed(
        "Format email salah",
        "Kata sandi terlalu pendek, minimum 8 karakter",
        "Harus diisi!"
    )
}

fun isErrorFieldEmpty() {
    isTextDisplayed("Harus diisi!")
}

fun isErrorFieldEmailInvalid() {
    inputText(inputTypeEmail, "habibi")
    isTextDisplayed("Format email salah")
}

fun isErrorFieldPasswordTooShort() {
    inputText(inputTypePassword, "123")
    isTextDisplayed("Kata sandi terlalu pendek, minimum 8 karakter")
}

fun isErrorFieldPasswordExceedLength() {
    inputText(inputTypePassword, "1234567890123456789012345678901234567890")
    isTextDisplayed("Kata sandi terlalu panjang, maksimum 32 karakter")
}

fun isErrorFieldNameTooShort() {
    inputText(inputTypeName, "ha")
    isTextDisplayed("Min. 3 karakter.")
}

fun isErrorFieldNameExceedLength() {
    inputText(inputTypeName, "habibihabibihabibihabibihabibihabibi")
    isTextDisplayed("Maks. 35 karakter.")
}

fun isErrorFieldNameInvalid() {
    inputText(inputTypeName, "habibi123")
    isTextDisplayed("Ketik namamu pakai huruf, ya.")
}

fun isDialogOfferLoginDisplayed() {
    isTextDisplayed(
        "E-mail sudah Terdaftar",
        "Lanjut masuk dengan e-mail ini\n$validEmail"
    )
    isDisplayed(
        com.tokopedia.dialog.R.id.dialog_btn_secondary,
        com.tokopedia.dialog.R.id.dialog_btn_primary
    )
}

fun isFailedMessageDisplayed() {
    isTextDisplayed(
        "Input tidak valid",
        "Email tidak valid",
        "Kata sandi tidak valid",
        "Nama tidak valid"
    )
}

fun inputThenClearFieldTextEmail() {
    inputText(inputTypeEmail, "habibi")
    clearText(inputTypeEmail)
}

fun inputThenClearFieldPassword() {
    inputText(inputTypePassword, "habibi")
    clearText(inputTypePassword)
}

fun inputThenClearFieldName() {
    inputText(inputTypeName, "habibi")
    clearText(inputTypeName)
}

fun inputValidValue() {
    inputText(inputTypeEmail, validEmail)
    inputText(inputTypePassword, "1234567890")
    inputText(inputTypeName, "Habibi")
}
