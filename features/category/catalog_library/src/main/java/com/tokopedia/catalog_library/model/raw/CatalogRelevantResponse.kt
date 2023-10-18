package com.tokopedia.catalog_library.model.raw

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogRelevantResponse(
    @SerializedName("catalogGetRelevant")
    @Expose
    val catalogGetRelevant: CatalogGetRelevant = CatalogGetRelevant()
) {
    data class CatalogGetRelevant(
        @SerializedName("catalogs")
        @Expose
        val catalogsList: ArrayList<Catalogs> = arrayListOf()
    )
    data class Catalogs(
        @SerializedName("id")
        @Expose
        val id: String? = "",
        @SerializedName("name")
        @Expose
        val name: String? = "",
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String? = "",
        @SerializedName("mobileUrl")
        @Expose
        val mobileUrl: String? = "",
        @SerializedName("applink")
        @Expose
        val applink: String? = "",
        @SerializedName("url")
        @Expose
        val url: String? = "",
        @SerializedName("brand")
        @Expose
        val brand: String? = ""
    )
}
