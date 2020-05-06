package com.tokopedia.home.beranda.domain.gql.feed


import com.google.gson.annotations.SerializedName

data class Banner(
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("bu_attribution")
        val buAttribution: String = "",
        @SerializedName("creative_name")
        val creativeName: String = "",
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("target")
        val target: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("galaxy_attribution")
        val galaxyAttribution: String = "",
        @SerializedName("persona")
        val persona: String = "",
        @SerializedName("brand_id")
        val brandId: String = "",
        @SerializedName("category_persona")
        val categoryPersona: String = ""
)