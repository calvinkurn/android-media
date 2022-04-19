package com.tokopedia.withdraw.auto_withdrawal.domain.model

import com.google.gson.annotations.SerializedName

data class AutoWDTNCResponse(
        @SerializedName("GetTNCAutoWD")
        val getTNCAutoWD: GetTNCAutoWD
)

data class GetTNCAutoWD(
        @SerializedName("code") val code: Int = 0,
        @SerializedName("data") val data: AWDTemplateData,
        @SerializedName("message") val message: String
)

data class AWDTemplateData(
        @SerializedName("template") val template: String
)
