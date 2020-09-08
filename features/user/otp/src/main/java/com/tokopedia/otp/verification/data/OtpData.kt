package com.tokopedia.otp.verification.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author rival
 * @created on 9/12/2019
 */

@Parcelize
data class OtpData(
        @Expose @SerializedName("userId") var userId: String = "",
        @Expose @SerializedName("msisdn") var msisdn: String = "",
        @Expose @SerializedName("email") var email: String = "",
        @Expose @SerializedName("otpType") var otpType: Int = 0,
        @Expose @SerializedName("otpMode") var otpMode: String = "",
        @Expose @SerializedName("canUseOtherMethod") var canUseOtherMethod: Boolean = false,
        @Expose @SerializedName("isShowChooseMethod") var isShowChooseMethod: Boolean = false,
        @Expose @SerializedName("source") var source: String = "",
        @Expose @SerializedName("userIdEnc") var userIdEnc: String = "",
        @Expose @SerializedName("accessToken") var accessToken: String = ""

        ) : Parcelable