package com.tokopedia.otp.verification.data

import com.tokopedia.config.GlobalConfig
import com.tokopedia.url.TokopediaUrl

/**
 * @author rival
 * @created on 9/12/2019
 */

object OtpConstant {

    const val OTP_DATA_EXTRA = "otp-data-extra"
    const val OTP_MODE_EXTRA = "otp-mode-extra"
    const val IS_MORE_THAN_ONE_EXTRA = "is-more-than-one-extra"
    const val OTP_WA_NOT_REGISTERED_TITLE = "otp-wa-not-registered-title"
    const val OTP_WA_NOT_REGISTERED_SUBTITLE = "otp-wa-not-registered-subtitle"
    const val OTP_WA_NOT_REGISTERED_IMG_LINK = "otp-wa-not-registered-img-link"

    const val KEY_DEFAULT_OTP_ROLLENCE = "otpdefault_an"

    private const val staging = "staging"

    val PIN_V2_SALT = getSalt()

    private fun getSalt(): String {
        return if (GlobalConfig.DEBUG && TokopediaUrl.getInstance().TYPE.value.lowercase() == staging) {
            "c456bbc2c9c746ffaf67787d7c59945d"
        } else {
            "b9f14c8ed04a41c7a5361b648a088b69"
        }
    }

    object OtpMode {
        const val SMS = "sms"
        const val WA = "whatsapp"
        const val EMAIL = "email"
        const val PIN = "PIN"
        const val SILENT_VERIFICATION = "silent_verif"
        const val GOOGLE_AUTH = "google_authenticator"
    }

    object OtpType {
        const val PHONE_NUMBER_VERIFICATION = 11
        const val REGISTER_PHONE_NUMBER = 116
        const val REGISTER_EMAIL = 126
        const val VERIFY_USER_CHANGE_PHONE_NUMBER = 200

        /* for 2FA flows */
        const val AFTER_LOGIN_PHONE = 148

        /* for 1FA flows */
        const val OTP_LOGIN_PHONE_NUMBER = 112
        const val RESET_PIN = 149

        const val INACTIVE_PHONE_VERIFY_EMAIL = 160
        const val INACTIVE_PHONE_VERIFY_PIN = 161
        const val INACTIVE_PHONE_VERIFY_NEW_PHONE = 162

        /* OTP Type: 168
        * This is actually OTP with phone number
        * this OTP type used when user register with email, and need OTP phone
        * this OTP type was implemented in Redefine Register Email
        * */
        const val PHONE_REGISTER_MANDATORY = 168

        const val ADD_BANK_ACCOUNT = 12
    }

    object StaticText {
        const val OTHER_METHOD_AND_CHANGE_PN_FOOTER_TEXT = "Berkendala dengan metode di atas?\n" +
            " Lihat metode lain atau nomor HP sudah berubah"
        const val OTHER_METHOD_FOOTER_TEXT = "Berkendala dengan metode di atas?\n Lihat metode lain"
        const val SMS_AND_CHANGE_PN_FOOTER_TEXT = "Berkendala dengan metode di atas?\n" +
            " Gunakan metode SMS atau nomor HP sudah berubah"
        const val SMS_METHOD_FOOTER_TEXT = "Berkendala dengan metode di atas?\n Gunakan metode SMS"

        const val SPAN_USE_OTHER_METHODS = "Lihat metode lain"
        const val SPAN_USE_SMS_METHOD = "Gunakan metode SMS"

        /**
         * @return [SMS_AND_CHANGE_PN_FOOTER_TEXT] if [linkType] == 0 && [spanMethods] == [SPAN_USE_SMS_METHOD]
         * @return else [SMS_METHOD_FOOTER_TEXT]
         * @return [OTHER_METHOD_AND_CHANGE_PN_FOOTER_TEXT] if [linkType] == 0 && [spanMethods] == [SPAN_USE_OTHER_METHODS]
         * @return else [OTHER_METHOD_FOOTER_TEXT]
         */
        fun spanFactory(spanMethods: String, linkType: Int): String {
            return when (spanMethods) {
                SPAN_USE_SMS_METHOD -> {
                    return if (linkType > 0) {
                        SMS_AND_CHANGE_PN_FOOTER_TEXT
                    } else {
                        SMS_METHOD_FOOTER_TEXT
                    }
                }
                SPAN_USE_OTHER_METHODS -> {
                    return if (linkType > 0) {
                        OTHER_METHOD_AND_CHANGE_PN_FOOTER_TEXT
                    } else {
                        OTHER_METHOD_FOOTER_TEXT
                    }
                } else -> ""
            }
        }
    }

    // Default OTP = off / using old otp flow
    const val DEFAULT_OTP_BEHAVIOR_ZERO = 0

    // Default OTP off and hide SMS OTP
    const val DEFAULT_OTP_BEHAVIOR_ONE = 1

    // Default OTP on and show SMS otp, including direct request
    const val DEFAULT_OTP_BEHAVIOR_TWO = 2

    // Default OTP on and hide SMS otp, including direct request
    const val DEFAULT_OTP_BEHAVIOR_THREE = 3
}
