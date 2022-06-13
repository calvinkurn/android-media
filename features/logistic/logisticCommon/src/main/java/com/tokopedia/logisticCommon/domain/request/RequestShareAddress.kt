package com.tokopedia.logisticCommon.domain.request

import com.google.gson.annotations.SerializedName

data class RequestShareAddress(
    @SerializedName("user_id")
    var userId: String,
    var phone: String,
    var email: String
)
