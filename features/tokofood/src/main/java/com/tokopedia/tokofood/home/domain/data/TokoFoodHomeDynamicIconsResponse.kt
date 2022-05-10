package com.tokopedia.tokofood.home.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokoFoodHomeDynamicIconsResponse (
    @Expose
    @SerializedName("dynamicHomeIcon")
    val dynamicIcon: DynamicHomeIcon = DynamicHomeIcon(),
)

data class DynamicHomeIcon (
    @Expose
    @SerializedName("dynamicIcon")
    val listDynamicIcon: List<DynamicIcon> = listOf(),
)

data class DynamicIcon(
    @Expose
    @SerializedName("id")
    val id: String = "-1",

    @Expose
    @SerializedName("applinks")
    val applinks: String = "",

    @Expose
    @SerializedName("imageUrl")
    val imageUrl: String = "",

    @Expose
    @SerializedName("name")
    val name: String = "",

    @Expose
    @SerializedName("url")
    val url: String = "",

    @Expose
    @SerializedName("bu_identifier")
    val bu_identifier: String = "",

    @SerializedName("galaxy_attribution")
    @Expose
    val galaxyAttribution: String = "",

    @SerializedName("persona")
    @Expose
    val persona: String = "",

    @SerializedName("brand_id")
    @Expose
    val brandId: String = "",

    @SerializedName("category_persona")
    @Expose
    val categoryPersona: String = "",

    @SerializedName("campaignCode")
    @Expose
    val campaignCode: String = "",

    @SerializedName("withBackground")
    @Expose
    val withBackground: Boolean = false
)