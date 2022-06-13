package com.tokopedia.otp.verification.domain.query

/**
 * Created by Ade Fulki on 01/06/20.
 */

object OtpModeListQuery {

    private const val otpType = "\$otpType"
    private const val userId = "\$userId"
    private const val msisdn = "\$msisdn"
    private const val email = "\$email"
    private const val timeUnix = "\$Timeunix"
    private const val authenticity = "\$AuthenticitySignature"

    val query: String = """
        query otp_mode_list($otpType: String!, $userId: String, $msisdn: String, $email: String, $timeUnix: String, $authenticity: String){
            OTPModeList(otpType: $otpType, userID: $userId, msisdn: $msisdn, email: $email, Timeunix: $timeUnix, AuthenticitySignature: $authenticity) {
                success
                message
                errorMessage
                otpDigit
                linkType
                enableTicker
                tickerTrouble
                defaultMode
                modeLists {
                    modeCode
                    modeText
                    otpListText
                    afterOtpListText
                    afterOtpListTextHtml
                    otpListImgUrl
                    usingPopUp
                    popUpHeader
                    popUpBody
                    countdown
                    otpDigit
                }
            }
        }
    """.trimIndent()
}