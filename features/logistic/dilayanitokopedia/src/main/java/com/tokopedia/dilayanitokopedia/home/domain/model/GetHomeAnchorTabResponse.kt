package com.tokopedia.dilayanitokopedia.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetHomeAnchorTabResponse(
    @SerializedName("getHomeIconV2")
    @Expose
    val response: GetHomeIconV2 = GetHomeIconV2()
) {
    data class GetHomeIconV2(
        @SerializedName("icons")
        @Expose
        val icons: List<Icon> = listOf()
    ) {
        data class Icon(
            @SerializedName("applinks")
            @Expose
            val applinks: String = "",
            @SerializedName("brandID")
            @Expose
            val brandID: String = "",
            @SerializedName("buIdentifier")
            @Expose
            val buIdentifier: String = "",
            @SerializedName("campaignCode")
            @Expose
            val campaignCode: String = "",
            @SerializedName("categoryPersona")
            @Expose
            val categoryPersona: String = "",
            @SerializedName("galaxyAttribution")
            @Expose
            val galaxyAttribution: String = "",
            @SerializedName("feParam")
            @Expose
            val feParam: String = "",
            @SerializedName("id")
            @Expose
            val id: Long = 0,
            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String = "",
            @SerializedName("name")
            @Expose
            val name: String = "",
            @SerializedName("page")
            @Expose
            val page: String = "",
            @SerializedName("persona")
            @Expose
            val persona: String = "",
            @SerializedName("url")
            @Expose
            val url: String = "",
            @SerializedName("withBackground")
            @Expose
            val withBackground: Boolean = false
        )
    }
}
