package com.tokopedia.accountprofile.settingprofile.addpin.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-09-04.
 * ade.hadian@tokopedia.com
 */

data class CheckPinPojo(
    @SerializedName("check_pin")
    var data: CheckPinData = CheckPinData()
)

data class CheckPinData(
    @SuppressLint("Invalid Data Type")
    @SerializedName("valid")
    var valid: Boolean = false,
    @SerializedName("error_message")
    var errorMessage: String = ""
)
