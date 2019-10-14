package com.tokopedia.profilecompletion.addpin.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-10-08.
 * ade.hadian@tokopedia.com
 */

data class SkipOtpPinPojo(
        @SerializedName("OTPSkipValidation")
        @Expose
        var data: SkipOtpPinData = SkipOtpPinData()
)

data class SkipOtpPinData(
        @SerializedName("skip_otp")
        @Expose
        var skipOtp: Boolean = false,
        @SerializedName("validate_token")
        @Expose
        var validateToken: String = "",
        @SerializedName("message")
        @Expose
        var message: String = "",
        @SerializedName("error_message")
        @Expose
        var errorMessage: String = ""
)