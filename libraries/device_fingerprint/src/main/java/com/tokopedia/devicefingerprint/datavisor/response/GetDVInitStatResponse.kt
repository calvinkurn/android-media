package com.tokopedia.devicefingerprint.datavisor.response

import com.google.gson.annotations.SerializedName

data class GetDVInitStatResponse(
    @SerializedName("getDVInitStat") val getDVInitStat: GetDVInitStat,
) {
    fun isError() = getDVInitStat.isError
    fun isExpire() = getDVInitStat.data.isExpire
}

data class GetDVInitStat(
    @SerializedName("is_error") val isError: Boolean,
    @SerializedName("data") val data: GetDVInitStatData,
)

data class GetDVInitStatData(
    @SerializedName("is_expire") val isExpire: Boolean
)