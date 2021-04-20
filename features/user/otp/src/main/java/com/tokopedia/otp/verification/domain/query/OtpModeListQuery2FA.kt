package com.tokopedia.otp.verification.domain.query

/**
 * Created by Yoris Prayogo on 07/09/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

object OtpModeListQuery2FA {

    private const val otpType = "\$otpType"
    private const val ValidateToken = "\$ValidateToken"
    private const val UserIDEnc = "\$UserIDEnc"

    val query: String = """
        query otp_mode_list($otpType: String!, $ValidateToken: String, $UserIDEnc: String){
            OTPModeList(otpType: $otpType, ValidateToken: $ValidateToken, UserIDEnc: $UserIDEnc) {
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