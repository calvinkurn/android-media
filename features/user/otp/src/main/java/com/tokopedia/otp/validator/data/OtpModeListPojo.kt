package com.tokopedia.otp.validator.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-10-21.
 * ade.hadian@tokopedia.com
 */

data class OtpModeListPojo(
        @SerializedName("OTPModeList")
        @Expose
        var data: OtpModeListData = OtpModeListData()
)

data class OtpModeListData(
        @SerializedName("success")
        @Expose
        var success: Boolean = false,
        @SerializedName("message")
        @Expose
        var message: String = "",
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("modeLists")
        @Expose
        var modeList: ArrayList<ModeListData> = arrayListOf()
)

data class ModeListData(
        @SerializedName("modeCode")
        @Expose
        var modeCode: Int = 0,
        @SerializedName("modeText")
        @Expose
        var modeText: String = "",
        @SerializedName("otpListText")
        @Expose
        var otpListText: String = "",
        @SerializedName("afterOtpListText")
        @Expose
        var afterOtpListText: String = "",
        @SerializedName("afterOtpListTextHtml")
        @Expose
        var afterOtpListTextHtml: String = "",
        @SerializedName("otpListImgUrl")
        @Expose
        var otpListImgUrl: String = "",
        @SerializedName("usingPopUp")
        @Expose
        var usingPopUp: Boolean = false,
        @SerializedName("popUpHeader")
        @Expose
        var popUpHeader: String = "",
        @SerializedName("popUpBody")
        @Expose
        var popUpBody: String = "",
        @SerializedName("countdown")
        @Expose
        var countdown: Boolean = false
)