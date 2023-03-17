package com.tokopedia.dilayanitokopedia.home.domain.model

import com.google.gson.annotations.SerializedName

data class GetHomeAnchorTabResponse(
    @SerializedName("getHomeIconV2")
    val response: GetHomeIconV2 = GetHomeIconV2()
) {
    data class GetHomeIconV2(
        @SerializedName("icons")
        val icons: List<Icon> = listOf()
    ) {
        data class Icon(
            @SerializedName("applinks")
            val applinks: String = "",
            @SerializedName("brandID")
            val brandID: String = "",
            @SerializedName("buIdentifier")
            val buIdentifier: String = "",
            @SerializedName("campaignCode")
            val campaignCode: String = "",
            @SerializedName("categoryPersona")
            val categoryPersona: String = "",
            @SerializedName("galaxyAttribution")
            val galaxyAttribution: String = "",
            @SerializedName("feParam")
            val feParam: String = "",
            @SerializedName("id")
            val id: Long = 0,
            @SerializedName("imageUrl")
            val imageUrl: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("page")
            val page: String = "",
            @SerializedName("persona")
            val persona: String = "",
            @SerializedName("url")
            val url: String = "",
            @SerializedName("withBackground")
            val withBackground: Boolean = false
        )
    }
}
