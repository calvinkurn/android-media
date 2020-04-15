package com.tokopedia.common_category.model.topAds

import com.google.gson.annotations.SerializedName

data class TopAdsResponse(

        @field:SerializedName("productAds")
        val productAds: ProductAds? = null
)