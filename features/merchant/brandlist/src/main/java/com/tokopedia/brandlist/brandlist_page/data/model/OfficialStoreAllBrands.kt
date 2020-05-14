package com.tokopedia.brandlist.brandlist_page.data.model

import com.google.gson.annotations.SerializedName

data class OfficialStoreAllBrands(
        @SerializedName("brands")
        val brands: List<Brand> = listOf(),
        @SerializedName("totalBrands")
        val totalBrands: Int = 0
)