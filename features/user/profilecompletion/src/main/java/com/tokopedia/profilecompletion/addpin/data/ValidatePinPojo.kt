package com.tokopedia.profilecompletion.addpin.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-09-04.
 * ade.hadian@tokopedia.com
 */

data class ValidatePinPojo(
    @SerializedName("validate_pin")
    var data: ValidatePinData = ValidatePinData()
)

data class ValidatePinData(
    @SuppressLint("Invalid Data Type")
    @SerializedName("valid")
    var valid: Boolean = false,
    @SerializedName("error_message")
    var errorMessage: String = "",
    @SerializedName("pin_attempted")
    var pinAttempted: Int = 0,
    @SerializedName("max_pin_attempt")
    var maxPinAttempt: Int = 0
)