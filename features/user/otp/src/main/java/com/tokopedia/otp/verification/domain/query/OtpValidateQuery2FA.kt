package com.tokopedia.otp.verification.domain.query

/**
 * Created by Yoris Prayogo on 07/09/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

object OtpValidateQuery2FA {

    private const val otpType = "\$otpType"
    private const val validateToken = "\$ValidateToken"
    private const val userIdEnc = "\$UserIDEnc"
    private const val mode = "\$mode"
    private const val code = "\$code"

    val query: String = """
        query otp_validate(
            $otpType: String,
            $validateToken: String,
            $userIdEnc: String,
            $mode: String,
            $code: String
        ){
            OTPValidate(
                otpType: $otpType,
                ValidateToken: $validateToken,
                UserIDEnc: $userIdEnc,
                mode: $mode,
                code: $code
            ) {
                success
                message
                errorMessage
                validateToken
                cookieList {
                    key
                    value
                    expire
                }
            }
        }
    """.trimIndent()
}