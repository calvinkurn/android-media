package com.tokopedia.loginregister.login.const

/**
 * Created by Yoris on 13/07/21.
 */

object LoginConstants {

    object Request {
        const val REQUEST_SECURITY_QUESTION = 104
        const val REQUEST_ADD_NAME = 109
        const val REQUEST_CHOOSE_ACCOUNT = 110
        const val REQUEST_LOGIN_PHONE = 112
        const val REQUEST_REGISTER_PHONE = 113
        const val REQUEST_ADD_NAME_REGISTER_PHONE = 114
        const val REQUEST_LOGIN_GOOGLE = 116
        const val REQUEST_PENDING_OTP_VALIDATE = 121
        const val REQUEST_ADD_PIN_AFTER_REGISTER_PHONE = 122
        const val REQUEST_CHOOSE_ACCOUNT_FINGERPRINT = 123
        const val REQUEST_VERIFY_BIOMETRIC = 124
        const val REQUEST_GOTO_SEAMLESS = 125
        const val REQUEST_CHOOSE_ACCOUNT_OCL = 126
        const val REQUEST_INIT_REGISTER_SDK = 127
        const val REQUEST_TIKTOK_LOGIN = 128
    }

    object RemoteConfigKey {
        const val KEY_TICKER_FROM_ATC = "android_user_ticker_from_atc"
        const val KEY_BANNER = "android_user_banner_login"
        const val KEY_REGISTER_PUSH_NOTIF = "android_user_register_otp_push_notif_login_page"
    }

    object MsSdkKey{
        const val LOGGED = "logged"
        const val REGISTERED: String = "registered"
        const val APPID: String = "573733"
    }

    object OtpType {
        const val OTP_SECURITY_QUESTION = 134
        const val OTP_LOGIN_PHONE_NUMBER = 112
        const val OTP_REGISTER_PHONE_NUMBER = 116
        const val OTP_TYPE_ACTIVATE = 143
    }

    object PrefKey {
        const val KEY_FIRST_INSTALL_SEARCH = "KEY_FIRST_INSTALL_SEARCH"
        const val KEY_FIRST_INSTALL_TIME_SEARCH = "KEY_IS_FIRST_INSTALL_TIME_SEARCH"
    }

    object LoginType {
        const val PHONE_TYPE = "phone"
        const val EMAIL_TYPE = "email"
    }

    object SourcePage {
        const val SOURCE_ACCOUNT = "account"
        const val SOURCE_ATC = "atc"
    }

    object MenuItemId {
        const val ID_ACTION_REGISTER = 111
        const val ID_ACTION_DEVOPS = 112
    }

    object DiscoverLoginId {
        const val GPLUS = "gplus"
        const val EMAIL = "local-email"
    }

    object RollenceKey {
        const val LOGIN_PAGE_BIOMETRIC = "and_biom_entry_point"
        const val DIRECT_LOGIN_BIOMETRIC = "loginbiom_an"
    }

    object AutoLogin {
        const val AUTO_LOGIN_EMAIL = "email"
        const val AUTO_LOGIN_PASS = "pw"
        const val IS_AUTO_LOGIN = "auto_login"
        const val IS_AUTO_FILL = "auto_fill"
        const val AUTO_FILL_EMAIL = "email"
    }
}
