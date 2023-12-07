package com.scp.auth.common.utils

object ScpConstants {
    const val ID_LANGUAGE = "id"
    const val TOKO_USER_TYPE = "toko_user"

    const val APP_LOCALE = "id-ID"

    const val TOKOPEDIA_CLIENT_ID = "tokopedia:consumer:android"
    const val LOGGER_SCP_AUTH_TAG = "SCP_AUTH_ERROR"
    const val ERROR_TYPE = "error_type"

    const val DEBUG_CLIENT_SECRET = "uPu4ieJOyPnf7sAS6ENCrBSvRMhF1g"
    const val GOTO_PIN_DEBUG_SECRET = "a8533f47-040b-49a1-b0c7-d818b9499a2f-ICP"

    const val KEY_FIRST_INSTALL_SEARCH = "KEY_FIRST_INSTALL_SEARCH"
    const val KEY_FIRST_INSTALL_TIME_SEARCH = "KEY_IS_FIRST_INSTALL_TIME_SEARCH"

    object OtpType {
        const val PHONE_NUMBER_VERIFICATION = 11
        const val REGISTER_PHONE_NUMBER = 116
        const val REGISTER_EMAIL = 126
        const val RESET_PASSWORD = 132
        const val VERIFY_USER_CHANGE_PHONE_NUMBER = 200

        /* for 2FA flows */
        const val AFTER_LOGIN_PHONE = 148

        /* for 1FA flows */
        const val OTP_LOGIN_PHONE_NUMBER = 112
        const val RESET_PIN = 149

        const val INACTIVE_PHONE_VERIFY_EMAIL = 160
        const val INACTIVE_PHONE_VERIFY_PIN = 161
        const val INACTIVE_PHONE_VERIFY_NEW_PHONE = 162
        const val SQCP = 169

        /* OTP Type: 168
        * This is actually OTP with phone number
        * this OTP type used when user register with email, and need OTP phone
        * this OTP type was implemented in Redefine Register Email
        * */
        const val PHONE_REGISTER_MANDATORY = 168

        const val ADD_BANK_ACCOUNT = 12
    }
}
