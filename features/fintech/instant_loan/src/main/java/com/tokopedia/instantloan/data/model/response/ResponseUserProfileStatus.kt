package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUserProfileStatus {

    @SerializedName("data")
    @Expose
    var userProfileLoanEntity: UserProfileLoanEntity? = null

    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("latency")
    @Expose
    var latency: String? = null
}
