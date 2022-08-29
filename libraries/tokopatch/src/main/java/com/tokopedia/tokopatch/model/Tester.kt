package com.tokopedia.tokopatch.model

import com.google.gson.annotations.SerializedName

data class Tester(
    @SerializedName("devices")
    val list: List<Device> = listOf()
) {
    data class Device(
        @SerializedName("name")
        val name: String,
        @SerializedName("deviceID")
        val deviceId: String
    )
}
