package com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.response

import com.google.gson.annotations.SerializedName

data class UpdateDynamicDataPassingResponse(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("error_messages")
    val errorMessages: List<String> = emptyList(),
    @SerializedName("dynamic_data")
    val dynamicData: String = ""
)
