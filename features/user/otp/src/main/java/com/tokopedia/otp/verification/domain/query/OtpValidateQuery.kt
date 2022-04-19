package com.tokopedia.otp.verification.domain.query

/**
 * Created by Ade Fulki on 01/06/20.
 */

object OtpValidateQuery {

    private const val code = "\$code"
    private const val otpType = "\$otpType"
    private const val msisdn = "\$msisdn"
    private const val fpData = "\$fpData"
    private const val getSL = "\$getSL"
    private const val email = "\$email"
    private const val mode = "\$mode"
    private const val signature = "\$signature"
    private const val timeUnix = "\$timeUnix"
    private const val userId = "\$userId"

    /* For hashed PIN only */
    private const val PIN = "\$PIN"
    private const val usePinHash = "\$UsePINHash"
    private const val pinHash = "\$PINHash"

    val query: String = """
        query otp_validate(
            $code: String,
            $otpType: String,
            $msisdn: String,
            $fpData: String,
            $getSL: String,
            $email: String,
            $mode: String,
            $signature: String,
            $timeUnix: String,
            $userId: Int,
            $PIN: String,
            $usePinHash: Boolean,
            $pinHash: String
        ){
            OTPValidate(
                code: $code, 
                otpType: $otpType, 
                msisdn: $msisdn, 
                fpData: $fpData, 
                getSL: $getSL, 
                email: $email, 
                mode: $mode, 
                signature: $signature, 
                time_unix: $timeUnix, 
                UserID: $userId,
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