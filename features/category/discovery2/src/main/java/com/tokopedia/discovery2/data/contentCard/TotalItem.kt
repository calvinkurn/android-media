package com.tokopedia.discovery2.data.contentCard

import com.google.gson.annotations.SerializedName

data class TotalItem(
    @SerializedName("item_count")
    val itemCount: Int? = 0,

    @SerializedName("item_count_text")
    val itemCountText: String? = "",

    @SerializedName("item_count_wording")
    val itemCountWording: String? = "",
)
