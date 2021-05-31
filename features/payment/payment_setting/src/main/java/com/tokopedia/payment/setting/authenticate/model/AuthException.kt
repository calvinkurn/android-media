package com.tokopedia.payment.setting.authenticate.model

sealed class AuthException: Throwable() {
    data class CheckOtpException(val phoneNumber: String) : AuthException()
}