package com.tokopedia.verification.verification.domain.query

import com.tokopedia.verification.verification.domain.query.OtpValidateQuery.PIN
import com.tokopedia.verification.verification.domain.query.OtpValidateQuery.pinHash
import com.tokopedia.verification.verification.domain.query.OtpValidateQuery.usePinHash

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
    private const val msisdn = "\$msisdn"

    val query: String = """
        query otp_validate(
            $otpType: String,
            $validateToken: String,
            $userIdEnc: String,
            $mode: String,
            $code: String,
            $msisdn: String,
            $PIN: String,
            $usePinHash: Boolean,
            $pinHash: String
        ){
            OTPValidate(
                otpType: $otpType,
                ValidateToken: $validateToken,
                UserIDEnc: $userIdEnc,
                mode: $mode,
                code: $code,
                msisdn: $msisdn,
                PIN: $PIN, 
                UsePINHash: $usePinHash, 
                PINHash: $pinHash
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
