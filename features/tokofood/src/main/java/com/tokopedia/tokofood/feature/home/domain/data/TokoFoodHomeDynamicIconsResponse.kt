package com.tokopedia.tokofood.feature.home.domain.data

import com.google.gson.annotations.SerializedName

data class TokoFoodHomeDynamicIconsResponse (
    @SerializedName("dynamicHomeIcon")
    val dynamicIcon: DynamicHomeIcon = DynamicHomeIcon(),
)

data class DynamicHomeIcon (
    @SerializedName("dynamicIcon")
    val listDynamicIcon: List<DynamicIcon> = listOf(),
)

data class DynamicIcon(
    @SerializedName("id")
    val id: String = "-1",

    @SerializedName("applinks")
    val applinks: String = "",

    @SerializedName("imageUrl")
    val imageUrl: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("bu_identifier")
    val bu_identifier: String = "",

    @SerializedName("galaxy_attribution")
    val galaxyAttribution: String = "",

    @SerializedName("persona")
    val persona: String = "",

    @SerializedName("brand_id")
    val brandId: String = "",

    @SerializedName("category_persona")
    val categoryPersona: String = "",

    @SerializedName("campaignCode")
    val campaignCode: String = "",

    @SerializedName("withBackground")
    val withBackground: Boolean = false
)