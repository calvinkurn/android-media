package com.tokopedia.catalog_library.model.raw

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogBrandsPopularResponse(
    @SerializedName("catalogGetBrandPopular")
    @Expose
    val catalogGetBrandPopular: CatalogGetBrandPopular = CatalogGetBrandPopular()
) {
    data class CatalogGetBrandPopular(
        @SerializedName("header")
        @Expose
        val header: CatalogBrandsPopularHeader = CatalogBrandsPopularHeader(),

        @SerializedName("brands")
        @Expose
        val brands: ArrayList<Brands> = arrayListOf()
    ) {
        data class CatalogBrandsPopularHeader(
            @SerializedName("code")
            @Expose
            val code: Int? = 0,

            @SerializedName("message")
            @Expose
            val message: String? = ""
        )
        data class Brands(
            @SerializedName("id")
            @Expose
            val id: String? = "",
            @SerializedName("name")
            @Expose
            val name: String? = "",
            @SerializedName("identifier")
            @Expose
            val identifier: String? = "",
            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String? = "",
            @SerializedName("catalogs")
            @Expose
            val catalogs: ArrayList<Catalogs> = arrayListOf()
        ) {
            data class Catalogs(
                @SerializedName("id")
                @Expose
                val id: String? = "",
                @SerializedName("name")
                @Expose
                val name: String? = "",
                @SerializedName("applink")
                @Expose
                val applink: String? = "",
                @SerializedName("imageUrl")
                @Expose
                val imageUrl: String? = "",
                @SerializedName("url")
                @Expose
                val url: String? = "",
                @SerializedName("mobileUrl")
                @Expose
                val mobileUrl: String? = ""
            )
        }
    }
}
