package com.tokopedia.devicefingerprint.datavisor.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VisorResponse(
        @SerializedName("getDeviceCrDetail")
        @Expose
        val deviceCrDetail: DeviceCrDetail = DeviceCrDetail()
)