package com.tokopedia.manageaddress.domain.response.shareaddress

import com.google.gson.annotations.SerializedName

data class KeroAddressError(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("detail")
    var detail: String = "",
    @SerializedName("category")
    var category: String = "",
    @SerializedName("message")
    var message: String = ""
)
