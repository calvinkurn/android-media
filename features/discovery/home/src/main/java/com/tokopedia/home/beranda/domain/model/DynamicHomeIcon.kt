package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder

data class DynamicHomeIcon (
    @Expose
    @SerializedName("dynamicIcon", alternate = ["icons"])
    val dynamicIcon: List<DynamicIcon> = listOf(),
    var type: Int = 1
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
        @SerializedName("bu_identifier", alternate = ["buIdentifier"])
        val bu_identifier: String = "",

        @SerializedName("galaxy_attribution", alternate = ["galaxyAttribution"])
        @Expose
        val galaxyAttribution: String = "",

        @SerializedName("persona")
        @Expose
        val persona: String = "",

        @SerializedName("brand_id", alternate = ["brandID"])
        @Expose
        val brandId: String = "",

        @SerializedName("category_persona", alternate = ["categoryPersona"])
        @Expose
        val categoryPersona: String = "",

        @SerializedName("campaignCode")
        @Expose
        val campaignCode: String = "",

        @SerializedName("withBackground")
        @Expose
        val withBackground: Boolean = false
    ) : ImpressHolder()
}
