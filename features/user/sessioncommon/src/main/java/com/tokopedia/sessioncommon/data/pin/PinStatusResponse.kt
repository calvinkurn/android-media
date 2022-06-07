package com.tokopedia.sessioncommon.data.pin

import com.google.gson.annotations.SerializedName

data class PinStatusResponse(
    @SerializedName("data")
    val data: PinStatusData = PinStatusData()
)

data class PinStatusData(
    @SerializedName("uh")
    val isNeedHash: Boolean = false,
    @SerializedName("error_message")
    val errorMessage: String = ""
)