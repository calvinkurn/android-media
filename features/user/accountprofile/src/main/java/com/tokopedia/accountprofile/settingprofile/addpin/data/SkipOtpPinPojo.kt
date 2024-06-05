package com.tokopedia.accountprofile.settingprofile.addpin.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-10-08.
 * ade.hadian@tokopedia.com
 */

data class SkipOtpPinPojo(
    @SerializedName("OTPSkipValidation")
    var data: SkipOtpPinData = SkipOtpPinData()
)

data class SkipOtpPinData(
    @SerializedName("skip_otp")
    var skipOtp: Boolean = false,
    @SerializedName("validate_token")
    var validateToken: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("error_message")
    var errorMessage: String = ""
)
