package com.tokopedia.otp.verification.domain.pojo

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetOtpModeListParam (
    @SerializedName("otpType")
    val otpType: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("msisdn")
    val msisdn: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("Timeunix")
    val timeUnix: String = "",
    @SerializedName("AuthenticitySignature")
    val authenticity: String = ""
): GqlParam
