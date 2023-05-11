package com.tokopedia.otp.verification.domain.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ade Fulki on 2019-10-21.
 * ade.hadian@tokopedia.com
 */

data class OtpModeListPojo(
        @SerializedName("OTPModeList")
        var data: OtpModeListData = OtpModeListData()
)

data class OtpModeListData(
        @SerializedName("success")
        var success: Boolean = false,
        @SerializedName("message")
        var message: String = "",
        @SerializedName("enableTicker")
        var enableTicker: Boolean = false,
        @SerializedName("tickerTrouble")
        var tickerTrouble: String = "",
        @SerializedName("errorMessage")
        var errorMessage: String = "",
        @SerializedName("otpDigit")
        var otpDigit: Int = 0,
        @SerializedName("linkType")
        var linkType: Int = 0,
        @SerializedName("defaultMode")
        var defaultMode: Int = 0,
        @SerializedName("defaultBehaviorMode")
        var defaultBehaviorMode: Int = 0,
        @SerializedName("switchAlternativeMethodTime")
        var switchAlternativeMethodTime: Int = 0,
        @SerializedName("modeLists")
        var modeList: ArrayList<ModeListData> = arrayListOf()
)

@Parcelize
data class ModeListData(
        @SerializedName("modeCode")
        var modeCode: Int = 0,
        @SerializedName("modeText")
        var modeText: String = "",
        @SerializedName("otpListText")
        var otpListText: String = "",
        @SerializedName("afterOtpListText")
        var afterOtpListText: String = "",
        @SerializedName("afterOtpListTextHtml")
        var afterOtpListTextHtml: String = "",
        @SerializedName("otpListImgUrl")
        var otpListImgUrl: String = "",
        @SerializedName("usingPopUp")
        var usingPopUp: Boolean = false,
        @SerializedName("popUpHeader")
        var popUpHeader: String = "",
        @SerializedName("popUpBody")
        var popUpBody: String = "",
        @SerializedName("directRequest")
        var directRequest: Boolean = false,
        @SerializedName("countdown")
        var countdown: Boolean = false,
        @SerializedName("otpDigit")
        var otpDigit: Int = 4  // default otp length 4 digits
): Parcelable
