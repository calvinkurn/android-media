package com.tokopedia.brandlist.brandlist_page.data.model

import com.google.gson.annotations.SerializedName

data class OfficialStoreBrandsRecommendation(
        @SerializedName("totalShops")
        val totalShops: Int = 0,
        @SerializedName("shops")
        val shops: List<Shop> = listOf(),
        @SerializedName("header")
        val header: Header = Header()
)