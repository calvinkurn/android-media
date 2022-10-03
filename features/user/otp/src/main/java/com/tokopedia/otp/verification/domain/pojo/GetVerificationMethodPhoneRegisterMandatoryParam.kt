package com.tokopedia.otp.verification.domain.pojo

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetVerificationMethodPhoneRegisterMandatoryParam(

    @SerializedName("otpType")
    var otpType: String = "",

    @SerializedName("msisdn")
    var msisdn: String = "",

    @SerializedName("email")
    var email: String = "",

    @SerializedName("ValidateToken")
    var validateToken: String = ""

) : GqlParam
