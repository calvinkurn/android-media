package com.tokopedia.loginregister.inactive_phone_number.view.model

sealed class StatusPhoneNumberResult(val throwable: Throwable? = null) {
    class SuccessHaveMultipleAccount : StatusPhoneNumberResult()
    class SuccessDoNotHaveMultipleAccount : StatusPhoneNumberResult()
    class Error(throwable: Throwable) : StatusPhoneNumberResult(throwable)
}