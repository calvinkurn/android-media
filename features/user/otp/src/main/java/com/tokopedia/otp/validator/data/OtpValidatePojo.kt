package com.tokopedia.otp.validator.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-10-21.
 * ade.hadian@tokopedia.com
 */

data class OtpValidatePojo(
        @SerializedName("OTPValidate")
        @Expose
        var data: OtpValidateData = OtpValidateData()
)

data class OtpValidateData(
        @SerializedName("success")
        @Expose
        var success: Boolean = false,
        @SerializedName("message")
        @Expose
        var message: String = "",
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("validateToken")
        @Expose
        var validateToken: String = ""
)