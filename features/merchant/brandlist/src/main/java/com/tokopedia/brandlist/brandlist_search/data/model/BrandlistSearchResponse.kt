package com.tokopedia.brandlist.brandlist_search.data.model

import com.google.gson.annotations.SerializedName

data class BrandlistSearchResponse(
        @SerializedName("OfficialStoreAllBrands")
        val officialStoreAllBrands: OfficialStoreAllBrands = OfficialStoreAllBrands()
)

data class OfficialStoreAllBrands(
        @SerializedName("brands")
        val brands: List<Brand> = listOf(),
        @SerializedName("totalBrands")
        val totalBrands: Int = 0
)

data class Brand(
        @SerializedName("defaultUrl")
        val defaultUrl: String = "",
        @SerializedName("exclusiveLogoURL")
        val exclusiveLogoURL: String = "",
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("isNew")
        val isNew: Int = 0,
        @SerializedName("logoUrl")
        val logoUrl: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("appsUrl")
        val appsUrl: String = ""
)
