package com.tokopedia.profilecompletion.common.model

import com.google.gson.annotations.SerializedName

data class CheckPinV2Response(
    @SerializedName("check_pin_v2")
    val data: CheckPinV2Data = CheckPinV2Data()
)

data class CheckPinV2Data(
    @SerializedName("valid")
    var valid: Boolean = false,
    @SerializedName("error_message")
    var errorMessage: String = "",
    @SerializedName("pin_token")
    var pinToken: String = ""
)
