package com.tokopedia.officialstore.official.data.model

import com.google.gson.annotations.SerializedName

data class OfficialStoreFeaturedShop(
    @SerializedName("shops")
    val featuredShops: MutableList<Shop> = mutableListOf()
) {
    data class Response(
            @SerializedName("OfficialStoreFeaturedShop")
            val officialStoreFeaturedShop : OfficialStoreFeaturedShop = OfficialStoreFeaturedShop(),
            @SerializedName("totalShops")
            val total: String
    )
}