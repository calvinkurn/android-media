package com.tokopedia.otp.verification.domain.data

/**
 * @author rival
 * @created on 9/12/2019
 */

const val ROLLANCE_KEY_MISCALL_OTP = "otp_miscall_new_ui"
const val TAG_AUTO_READ = "autoread"

object OtpConstant {

    const val OTP_DATA_EXTRA = "otp-data-extra"
    const val OTP_MODE_EXTRA = "otp-mode-extra"
    const val IS_MORE_THAN_ONE_EXTRA = "is-more-than-one-extra"
    const val OTP_WA_NOT_REGISTERED_TITLE = "otp-wa-not-registered-title"
    const val OTP_WA_NOT_REGISTERED_SUBTITLE = "otp-wa-not-registered-subtitle"
    const val OTP_WA_NOT_REGISTERED_IMG_LINK = "otp-wa-not-registered-img-link"

    object OtpMode {
        const val SMS = "sms"
        const val WA = "whatsapp"
        const val EMAIL = "email"
        const val MISCALL = "misscall"
        const val PIN = "PIN"
        const val SILENT_VERIFICATION = "silent_verif"
        const val GOOGLE_AUTH = "google_authenticator"
    }

    object OtpType {
        const val PHONE_NUMBER_VERIFICATION = 11
        const val REGISTER_PHONE_NUMBER = 116
        const val REGISTER_EMAIL = 126
        const val VERIFY_USER_CHANGE_PHONE_NUMBER = 200
        const val AFTER_LOGIN_PHONE = 148
        const val RESET_PIN = 149

        const val INACTIVE_PHONE_VERIFY_EMAIL = 160
        const val INACTIVE_PHONE_VERIFY_PIN = 161
        const val INACTIVE_PHONE_VERIFY_NEW_PHONE = 162
    }
}