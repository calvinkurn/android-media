package com.tokopedia.discovery2.data.cpmtopads

import com.google.gson.annotations.SerializedName

data class CpmTopAdsResponse(

        @SerializedName("displayAdsV3")
        val displayAdsV3: DisplayAdsV3? = null
)