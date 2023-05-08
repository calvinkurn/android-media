package com.tokopedia.devicefingerprint.submitdevice.payload

import com.google.gson.annotations.SerializedName

data class InsertDeviceInfoPayload(
    @SerializedName("content")
    val content: String,
    @SerializedName("identifier")
    val identifier: String,
    @SerializedName("version")
    val version: String = "1"
)
