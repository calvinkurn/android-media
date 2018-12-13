package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class ResponseUserProfileStatus(

        @SerializedName("data")
        var userProfileLoanEntity: UserProfileLoanEntity? = null,

        @SerializedName("code")
        var code: String? = null,

        @SerializedName("latency")
        var latency: String? = null
)
