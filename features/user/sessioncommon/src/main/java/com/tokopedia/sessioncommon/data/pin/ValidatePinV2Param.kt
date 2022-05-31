package com.tokopedia.sessioncommon.data.pin

import com.google.gson.annotations.SerializedName

data class ValidatePinV2Param(
    @SerializedName("pin")
    val pin: String,
    @SerializedName("h")
    val hash: String
)