package com.tokopedia.devicefingerprint.datavisor.payload

import com.google.gson.annotations.SerializedName

data class DeviceInitPayload(
        @SerializedName("key") val key: String,
        @SerializedName("user_id") val userId: Long = 0L,
        @SerializedName("retry_count") val retryCount: Int,
        @SerializedName("error_message") val errorMessage: String,
        @SerializedName("device_type") val deviceType: String = "android"
)
