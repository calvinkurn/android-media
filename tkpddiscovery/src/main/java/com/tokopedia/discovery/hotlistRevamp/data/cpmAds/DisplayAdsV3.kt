package com.tokopedia.discovery.hotlistRevamp.data.cpmAds

import com.google.gson.annotations.SerializedName

data class DisplayAdsV3(

        @field:SerializedName("data")
        val data: List<DataItem?>? = null
)