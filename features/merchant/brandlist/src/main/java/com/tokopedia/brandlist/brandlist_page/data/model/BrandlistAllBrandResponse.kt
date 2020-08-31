package com.tokopedia.brandlist.brandlist_page.data.model

import com.google.gson.annotations.SerializedName

data class BrandlistAllBrandResponse(
        @SerializedName("OfficialStoreAllBrands")
        val officialStoreAllBrands: OfficialStoreAllBrands = OfficialStoreAllBrands()
)