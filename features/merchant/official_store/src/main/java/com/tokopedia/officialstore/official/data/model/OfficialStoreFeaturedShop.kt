package com.tokopedia.officialstore.official.data.model

import com.google.gson.annotations.SerializedName

data class OfficialStoreFeaturedShop(
    @SerializedName("shops")
    val featuredShops: MutableList<Shop> = mutableListOf(),
    @SerializedName("totalShops")
    val total: String = "",
    @SerializedName("header")
    val header: HeaderShop = HeaderShop()
) {
    data class Response(
            @SerializedName("OfficialStoreFeaturedShop")
            val officialStoreFeaturedShop : OfficialStoreFeaturedShop = OfficialStoreFeaturedShop()
    )
}