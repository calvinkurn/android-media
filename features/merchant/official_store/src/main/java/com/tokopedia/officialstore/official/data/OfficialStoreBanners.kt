package com.tokopedia.officialstore.official.data

import com.google.gson.annotations.SerializedName

data class OfficialStoreBanners(
    @SerializedName("banners")
    val banners: MutableList<Banner> = mutableListOf()
) {
    data class Response(
            @SerializedName("OfficialStoreBanners")
            val officialStoreBanners : OfficialStoreBanners = OfficialStoreBanners(),
            @SerializedName("total")
            val total: String
    )
}