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
    val errorMessage: Boolean = false,
    @SerializedName("pin_attempted")
    val pinAttempted: Boolean = false,
    @SerializedName("max_pin_attempt")
    val maxPinAttempted: Boolean = false
)
