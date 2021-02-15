package com.tokopedia.devicefingerprint.datavisor.response

import com.google.gson.annotations.SerializedName

data class SubmitDeviceInitResponse(
        @SerializedName("is_error") val isError: Boolean
)
