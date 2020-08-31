package com.tokopedia.brandlist.brandlist_page.data.model

import com.google.gson.annotations.SerializedName

data class BrandlistNewBrandResponse(
        @SerializedName("OfficialStoreBrandsRecommendation")
        val officialStoreBrandsRecommendation: OfficialStoreBrandsRecommendation = OfficialStoreBrandsRecommendation()
)