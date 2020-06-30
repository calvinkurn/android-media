package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder

data class DynamicHomeIcon (
    @Expose
    @SerializedName("dynamicIcon")
    val dynamicIcon: List<DynamicIcon> = listOf()
){
    data class UseCaseIcon(
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
        val url: String = ""
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
        val galaxyAttribution: String = "",

        @SerializedName("persona")
        val persona: String = "",

        @SerializedName("brand_id")
        val brandId: String = "",

        @SerializedName("category_persona")
        val categoryPersona: String = ""
    ) : ImpressHolder()
}