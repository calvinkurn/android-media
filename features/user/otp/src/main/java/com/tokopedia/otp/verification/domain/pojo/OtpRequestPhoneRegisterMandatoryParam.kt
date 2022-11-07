package com.tokopedia.otp.verification.domain.pojo

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class OtpRequestPhoneRegisterMandatoryParam(

    @SerializedName("otpType")
    val otpType: String = "",

    @SerializedName("mode")
    val mode: String = "",

    @SerializedName("msisdn")
    val msisdn: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("ValidateToken")
    val validateToken: String = "",

    @SerializedName("otpDigit")
    val otpDigit: Int = 6

) : GqlParam
