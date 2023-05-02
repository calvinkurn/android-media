package com.tokopedia.mvc.data.request

import com.google.gson.annotations.SerializedName

data class GetTargetedTickerRequest(
    @SerializedName("Page") val page: String,
    @SerializedName("Target") val targets: List<Target>
) {
    data class Target(
        @SerializedName("Type") val type: String,
        @SerializedName("Values") val value: List<String>
    )
}
