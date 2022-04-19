package com.tokopedia.otp.qrcode.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VerifyQrPojo(
        @SerializedName("VerifyQR")
        @Expose
        var data: VerifyQrData = VerifyQrData()
)

data class VerifyQrData(
        @SerializedName("success")
        @Expose
        var success: Boolean = false,
        @SerializedName("message")
        @Expose
        var message: String = "",
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("Imglink")
        @Expose
        var imglink: String = "",
        @SerializedName("MessageTitle")
        @Expose
        var messageTitle: String = "",
        @SerializedName("MessageBody")
        @Expose
        var messageBody: String = "",
        @SerializedName("ButtonType")
        @Expose
        var buttonType: String = "",
        @SerializedName("Status")
        @Expose
        var status: String = "",
        var approvalStatus: String = ""
)