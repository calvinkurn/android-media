package com.tokopedia.createpost.data.pojo.productsuggestion.affiliate


import com.google.gson.annotations.SerializedName

data class ExplorePageSection(
        @SerializedName("items")
    val items: List<AffiliateProductItem> = listOf(),
        @SerializedName("subtitle")
    val subtitle: String = "",
        @SerializedName("title")
    val title: String = "",
        @SerializedName("type")
    val type: String = ""
)