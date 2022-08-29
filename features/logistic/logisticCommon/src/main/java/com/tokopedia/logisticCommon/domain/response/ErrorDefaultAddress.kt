package com.tokopedia.logisticCommon.domain.response

import com.google.gson.annotations.SerializedName

data class ErrorDefaultAddress(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("detail")
    var detail: String = ""
)