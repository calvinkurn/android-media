package com.tokopedia.affiliate.feature.explore.data.pojo.section

import com.google.gson.annotations.SerializedName

data class ExplorePageSection(
        @SerializedName("items")
        val items: List<SectionItem> = listOf(),
        @SerializedName("subtitle")
        val subtitle: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("type")
        val type: String = ""
)