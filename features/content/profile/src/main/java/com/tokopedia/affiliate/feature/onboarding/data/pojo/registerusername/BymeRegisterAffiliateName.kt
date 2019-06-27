package com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BymeRegisterAffiliateName(
        @SerializedName("success")
        @Expose
        var isSuccess: Boolean = false,

        @SerializedName("error")
        @Expose
        var error: Error = Error()
)
