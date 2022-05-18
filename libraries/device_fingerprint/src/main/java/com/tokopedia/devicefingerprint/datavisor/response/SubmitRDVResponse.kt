package com.tokopedia.devicefingerprint.datavisor.response

import com.google.gson.annotations.SerializedName

data class SubmitRDVResponse(
    @SerializedName("subRDVInit") val subRDVInit: SubRDVInit,
) {
    fun isError() = subRDVInit.isError
}

data class SubRDVInit(
    @SerializedName("is_error") val isError: Boolean,
)