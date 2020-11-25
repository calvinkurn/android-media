package com.tokopedia.hotlist.data.cpmAds

import com.google.gson.annotations.SerializedName

data class CpmTopAdsResponse(

        @field:SerializedName("displayAdsV3")
        val displayAdsV3: DisplayAdsV3? = null
)