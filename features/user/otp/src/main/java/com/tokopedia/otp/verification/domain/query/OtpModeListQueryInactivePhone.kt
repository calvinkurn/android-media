package com.tokopedia.otp.verification.domain.query


object OtpModeListQueryInactivePhone {

    private const val otpType = "\$otpType"
    private const val userId = "\$userId"
    private const val msisdn = "\$msisdn"
    private const val email = "\$email"
    private const val ValidateToken = "\$ValidateToken"
    private const val UserIDEnc = "\$UserIDEnc"

    val query: String = """
        query otp_mode_list($otpType: String!, $userId: String, $msisdn: String, $email: String, $ValidateToken: String, $UserIDEnc: String){
            OTPModeList(otpType: $otpType, userID: $userId, msisdn: $msisdn, email: $email, ValidateToken: $ValidateToken, UserIDEnc: $UserIDEnc) {
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