package com.tokopedia.otp.verification.domain.query

/**
 * Created by Ade Fulki on 01/06/20.
 */

object OtpModeListQuery {

    private const val otpType = "\$otpType"
    private const val userId = "\$userId"
    private const val msisdn = "\$msisdn"
    private const val email = "\$email"

    val query: String = """
        query otp_mode_list($otpType: String!, $userId: String, $msisdn: String, $email: String){
            OTPModeList(otpType: $otpType, userID: $userId, msisdn: $msisdn, email: $email) {
                success
                message
                errorMessage
                otpDigit
                linkType
                enableTicker
                tickerTrouble
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