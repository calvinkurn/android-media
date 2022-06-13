package com.tokopedia.profilecompletion.addpin.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-09-04.
 * ade.hadian@tokopedia.com
 */

data class CheckPinPojo(
    @SerializedName("check_pin")
    @Expose
    var data: CheckPinData = CheckPinData()
)

data class CheckPinData(
    @SerializedName("valid")
    @Expose
    var valid: Boolean = false,
    @SerializedName("error_message")
    @Expose
    var errorMessage: String = ""
)