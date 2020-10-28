package com.tokopedia.otp.verification.domain.query

/**
 * Created by Ade Fulki on 01/06/20.
 */

object OtpRequestQuery {

    private const val otpType = "\$otpType"
    private const val mode = "\$mode"
    private const val msisdn = "\$msisdn"
    private const val email = "\$email"
    private const val otpDigit = "\$otpDigit"

    private const val validateToken = "\$ValidateToken"
    private const val userIdEnc = "\$UserIDEnc"

    val query: String = """
        query otp_request($otpType: String!, $mode: String, $msisdn: String, $email: String, $otpDigit: Int){
            OTPRequest(otpType: $otpType, mode: $mode, msisdn: $msisdn, email: $email, otpDigit: $otpDigit) {
                success
                message
                errorMessage
                prefixMisscall
            }
        }
    """.trimIndent()

    val query2FA: String = """
        query otp_request($otpType: String!, $mode: String, $msisdn: String, $email: String, $otpDigit: Int, $validateToken: String, $userIdEnc: String){
            OTPRequest(otpType: $otpType, mode: $mode, msisdn: $msisdn, email: $email, otpDigit: $otpDigit, UserIDEnc: $userIdEnc, ValidateToken: $validateToken) {
                success
                message
                errorMessage
                prefixMisscall
            }
        }
    """.trimIndent()

}