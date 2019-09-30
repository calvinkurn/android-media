package com.tokopedia.discovery.categoryrevamp.data.topAds

import com.google.gson.annotations.SerializedName

data class TopAdsResponse(

        @field:SerializedName("productAds")
        val productAds: ProductAds? = null
)