package com.tokopedia.devicefingerprint.response

import com.google.gson.annotations.SerializedName

data class SubmitDeviceInfoResponse(
        @SerializedName("is_error") val isError: Boolean
)
