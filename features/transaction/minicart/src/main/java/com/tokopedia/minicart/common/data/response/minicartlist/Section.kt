package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Section(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("details")
    val details: List<SectionDetail> = emptyList()
)
