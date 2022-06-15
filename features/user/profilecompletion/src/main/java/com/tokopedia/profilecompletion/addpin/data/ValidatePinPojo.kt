package com.tokopedia.profilecompletion.addpin.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-09-04.
 * ade.hadian@tokopedia.com
 */

data class ValidatePinPojo(
    @SerializedName("validate_pin")
    @Expose
    var data: ValidatePinData = ValidatePinData()
)

data class ValidatePinData(
    @SerializedName("valid")
    @Expose
    var valid: Boolean = false,
    @SerializedName("error_message")
    @Expose
    var errorMessage: String = "",
    @SerializedName("pin_attempted")
    @Expose
    var pinAttempted: Int = 0,
    @SerializedName("max_pin_attempt")
    @Expose
    var maxPinAttempt: Int = 0
)