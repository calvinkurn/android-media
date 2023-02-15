package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class SectionDetail(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("value")
    val value: String = ""
)
