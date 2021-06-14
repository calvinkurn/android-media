package com.tokopedia.loginregister.common.error

object LoginErrorCode {
    const val ERROR_ON_FACEBOOK_CATCH_SUCCESS = "error_on_facebook_catch_success"
    const val ERROR_ON_FACEBOOK_CREDENTIAL = "error_on_facebook_credential"
    const val ERROR_ON_FACEBOOK = "error_on_facebook"

    const val ERROR_ON_GMAIL_NULL_EMAIL = "error_on_gmail_null_email"
    const val ERROR_ON_GMAIL_NULL_ACCOUNT = "error_on_gmail_null_account"
    const val ERROR_ON_GMAIL_CATCH = "error_on_gmail_catch"

    const val ERROR_EMAIL_TOKEN_EXCEPTION = "error_email_token_exception"
    const val ERROR_EMAIL_UNKNOWN = "error_email_unknown"
    const val ERROR_GET_USER_INFO = "error_get_user_info"

    const val ERROR_FACEBOOK = "error_facebook"
    const val ERROR_FACEBOOK_PHONE = "error_facebook_phone"

    const val ERROR_GMAIL = "error_gmail"

    const val ERROR_ACTIVATE_USER = "error_activate_user"

    const val ERROR_ACTIVATION_AFTER_RELOGIN = "error_activation_after_relogin"
    const val ERROR_SQ_AFTER_RELOGIN = "error_sq_after_relogin"
}