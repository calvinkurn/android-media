package com.tokopedia.devicefingerprint.crysp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CryspResponse(
        @SerializedName("getDeviceCrDetail")
        @Expose
        val deviceCrDetail: DeviceCrDetail = DeviceCrDetail()
)