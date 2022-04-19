package com.tokopedia.officialstore.official.data.model

import com.google.gson.annotations.SerializedName

data class OfficialStoreBanners(
    @SerializedName("slides")
    val banners: MutableList<Banner> = mutableListOf(),
    var isCache: Boolean = false
) {
    data class Response(
            @SerializedName("slides")
            val officialStoreBanners : OfficialStoreBanners = OfficialStoreBanners()
    )
}