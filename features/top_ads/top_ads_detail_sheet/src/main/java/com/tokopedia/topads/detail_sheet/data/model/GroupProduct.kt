package com.tokopedia.topads.detail_sheet.data.model


import com.google.gson.annotations.SerializedName

data class GroupProduct(
        @SerializedName("data")
    val `data`: List<Data> = ArrayList(),
        @SerializedName("meta")
    val meta: Meta = Meta(),
        @SerializedName("page")
    val page: Page = Page()
)