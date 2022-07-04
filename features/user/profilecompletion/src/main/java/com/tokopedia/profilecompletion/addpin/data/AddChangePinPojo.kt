package com.tokopedia.profilecompletion.addpin.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-09-03.
 * ade.hadian@tokopedia.com
 */

data class AddPinPojo(
    @SerializedName("create_pin")
    var data: AddChangePinData = AddChangePinData()
)

data class ChangePinPojo(
    @SerializedName("update_pin")
    var data: AddChangePinData = AddChangePinData()
)

data class AddChangePinData(
    @SerializedName("success")
    var success: Boolean = false,
    @SerializedName("errors")
    var errorAddChangePinData: List<ErrorAddChangePinData> = arrayListOf()
)

data class ErrorAddChangePinData(
    @SerializedName("message")
    var message: String = ""
)