package com.tokopedia.discovery2.data.quickcouponresponse

import com.google.gson.annotations.SerializedName

data class VerificationStatus(
        @SerializedName("phone_verified")
        val phoneVerified: Boolean? = false
)