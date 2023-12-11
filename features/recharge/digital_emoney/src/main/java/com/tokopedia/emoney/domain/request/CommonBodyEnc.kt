package com.tokopedia.emoney.domain.request

import com.google.gson.annotations.SerializedName

data class CommonBodyEnc(
    @SerializedName("encKey")
    var encKey: String = "",
    @SerializedName("encPayload")
    var encPayload: String = "",
)


