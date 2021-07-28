package com.tokopedia.loginregister.registerinitial.const

/**
 * Created by Yoris on 13/07/21.
 */

object RegisterConstants {
    object Request {
        const val REQUEST_REGISTER_EMAIL = 101
        const val REQUEST_CREATE_PASSWORD = 102
        const val REQUEST_SECURITY_QUESTION = 103
        const val REQUEST_VERIFY_PHONE_REGISTER_PHONE = 105
        const val REQUEST_ADD_NAME_REGISTER_PHONE = 107
        const val REQUEST_VERIFY_PHONE_TOKOCASH = 108
        const val REQUEST_CHOOSE_ACCOUNT = 109
        const val REQUEST_CHANGE_NAME = 111
        const val REQUEST_LOGIN_GOOGLE = 112
        const val REQUEST_OTP_VALIDATE = 113
        const val REQUEST_PENDING_OTP_VALIDATE = 114
        const val REQUEST_ADD_PIN = 115
    }

    object OtpType {
        const val OTP_TYPE_ACTIVATE = 143
        const val OTP_TYPE_REGISTER = 126
        const val OTP_SECURITY_QUESTION = 134
        const val OTP_LOGIN_PHONE_NUMBER = 112
        const val OTP_REGISTER_PHONE_NUMBER = 116
    }

    object RemoteConfigKey {
        const val REMOTE_CONFIG_KEY_TICKER_FROM_ATC = "android_user_ticker_from_atc"
        const val REMOTE_CONFIG_KEY_BANNER_REGISTER = "android_user_banner_register"
        const val REMOTE_CONFIG_KEY_REGISTER_PUSH_NOTIF = "android_user_register_otp_push_notif_register_page"
    }
}