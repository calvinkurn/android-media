package com.tokopedia.sessioncommon.data.pin

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ValidatePinV2Response(
    @SerializedName("validate_pin_v2")
    val validatePinV2Data: ValidatePinV2Data = ValidatePinV2Data()
)

data class ValidatePinV2Data(
    @SuppressLint("Invalid Data Type")
    @SerializedName("valid")
    val valid: Boolean = false,
    @SerializedName("error_message")
    val errorMessage: String = "",
    @SerializedName("pin_attempted")
    val pinAttempted: Int = 0,
    @SerializedName("max_pin_attempt")
    val maxPinAttempted: Int = 0
)
