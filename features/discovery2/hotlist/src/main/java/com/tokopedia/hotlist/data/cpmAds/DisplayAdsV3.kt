package com.tokopedia.hotlist.data.cpmAds

import com.google.gson.annotations.SerializedName

data class DisplayAdsV3(

        @field:SerializedName("data")
        val data: List<DataItem?>? = null
)