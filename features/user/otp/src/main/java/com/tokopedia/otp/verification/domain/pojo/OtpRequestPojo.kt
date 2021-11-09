package com.tokopedia.otp.verification.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-10-21.
 * ade.hadian@tokopedia.com
 */

data class OtpRequestPojo(
        @SerializedName("OTPRequest")
        @Expose
        var data: OtpRequestData = OtpRequestData()
)

data class OtpRequestData(
        @SerializedName("success")
        @Expose
        var success: Boolean = false,
        @SerializedName("message")
        @Expose
        var message: String = "",
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("prefixMisscall")
        @Expose
        var prefixMisscall: String = "",
        @SerializedName("message_title")
        @Expose
        var messageTitle: String = "",
        @SerializedName("message_sub_title")
        @Expose
        var messageSubTitle: String = "",
        @SerializedName("message_img_link")
        @Expose
        var messageImgLink: String = "",
        @SerializedName("error_code")
        @Expose
        var errorCode: String = ""
)