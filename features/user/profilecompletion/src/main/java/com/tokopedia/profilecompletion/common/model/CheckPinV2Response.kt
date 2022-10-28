package com.tokopedia.profilecompletion.common.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class CheckPinV2Response(
    @SerializedName("check_pin_v2")
    val data: CheckPinV2Data = CheckPinV2Data()
)

data class CheckPinV2Data(
    @SuppressLint("Invalid Data Type")
    @SerializedName("valid")
    var valid: Boolean = false,
    @SerializedName("error_message")
    var errorMessage: String = "",
    @SerializedName("pin_token")
    var pinToken: String = ""
)
