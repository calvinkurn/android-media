package com.tokopedia.otp.verification.domain.pojo

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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
        @SerializedName("enableTicker")
        @Expose
        var enableTicker: Boolean = false,
        @SerializedName("tickerTrouble")
        @Expose
        var tickerTrouble: String = "",
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = "",
        @SerializedName("otpDigit")
        @Expose
        var otpDigit: Int = 0,
        @SerializedName("linkType")
        @Expose
        var linkType: Int = 0,
        @SerializedName("modeLists")
        @Expose
        var modeList: ArrayList<ModeListData> = arrayListOf()
)

@Parcelize
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
        var countdown: Boolean = false,
        @SerializedName("otpDigit")
        @Expose
        var otpDigit: Int = 4  // default otp length 4 digits
): Parcelable