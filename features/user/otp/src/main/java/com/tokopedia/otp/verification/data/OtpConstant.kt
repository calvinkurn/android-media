package com.tokopedia.otp.verification.domain.data

/**
 * @author rival
 * @created on 9/12/2019
 */

object OtpConstant {

    const val OTP_DATA_EXTRA = "otp-data-extra"
    const val OTP_MODE_EXTRA = "otp-mode-extra"

    object OtpMode {
        const val SMS = "sms"
        const val CALL = "call"
        const val EMAIL = "email"
        const val MISCALL = "misscall"
    }

    object OtpType {
        const val SECURITY_QUESTION = 134
        const val PHONE_NUMBER_VERIFICATION = 11
        const val CHANGE_PHONE_NUMBER = 20
        const val LOGIN_PHONE_NUMBER = 112
        const val REGISTER_PHONE_NUMBER = 116
        const val REGISTER_EMAIL = 126
        const val CHECKOUT_DIGITAL = 16
        const val ADD_BANK_ACCOUNT = 12
        const val VERIFY_USER_CHANGE_PHONE_NUMBER = 200
        const val VERIFY_AUTH_CREDIT_CARD = 122
        const val PENDING_EMAIL = 143
        const val TOKOCASH = -1
    }
}