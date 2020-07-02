package com.tokopedia.discovery2.data.quickcouponresponse

import com.google.gson.annotations.SerializedName

data class PhoneVerificationResponse(
        @SerializedName("profile")
        val verificationStatus: VerificationStatus?
)