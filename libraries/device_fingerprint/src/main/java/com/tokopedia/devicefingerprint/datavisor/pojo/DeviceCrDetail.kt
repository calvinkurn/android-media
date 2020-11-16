package com.tokopedia.devicefingerprint.datavisor.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeviceCrDetail(

        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("message")
        @Expose
        val message: String = ""
)