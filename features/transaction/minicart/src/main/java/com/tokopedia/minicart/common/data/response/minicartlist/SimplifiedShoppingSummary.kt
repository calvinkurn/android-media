package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class SimplifiedShoppingSummary(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("sections")
    val sections: List<Section> = emptyList()
)
