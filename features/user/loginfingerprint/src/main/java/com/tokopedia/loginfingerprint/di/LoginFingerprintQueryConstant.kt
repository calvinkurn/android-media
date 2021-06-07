package com.tokopedia.loginfingerprint.di

/**
 * Created by Yoris Prayogo on 2020-01-29.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

object LoginFingerprintQueryConstant {
    const val QUERY_REGISTER_FINGERPRINT = "register_fingerprint"
    const val QUERY_VALIDATE_FINGERPRINT = "validate_fingerprint"

    const val PARAM_ID = "id"

    const val PARAM_USER_ID = "UserID"
    const val PARAM_OTP_TYPE = "otpType"
    const val PARAM_PUBLIC_KEY = "publicKey"
    const val PARAM_SIGNATURE = "signature"
    const val PARAM_MODE = "mode"
    const val PARAM_DATETIME = "datetime"
    const val PARAM_TIME_UNIX = "time_unix"

    const val VALIDATE_OTP_TYPE = 145
    const val VALIDATE_MODE = "fingerprint"
}