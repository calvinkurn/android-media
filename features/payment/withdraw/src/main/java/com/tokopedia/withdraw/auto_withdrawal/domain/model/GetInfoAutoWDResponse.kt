package com.tokopedia.withdraw.auto_withdrawal.domain.model

import com.google.gson.annotations.SerializedName

data class GetInfoAutoWDResponse(
        @SerializedName("GetInfoAutoWD")
        val getInfoAutoWD: GetInfoAutoWD
)

data class GetInfoAutoWD(
        @SerializedName("code") val code: Int = 0,
        @SerializedName("data") val data: ArrayList<InfoAutoWDData>,
        @SerializedName("message") val message: String
)

data class InfoAutoWDData(
        @SerializedName("title") val title: String,
        @SerializedName("desc") val description: String,
        @SerializedName("icon") val icon: String
)
