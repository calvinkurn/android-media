package com.tokopedia.discovery2.data.cpmtopads

import com.google.gson.annotations.SerializedName


data class DataItem(

        @SerializedName("applinks")
        val applinks: String? = "",

        @SerializedName("ad_click_url")
        val adClickUrl: String? = "",

        @SerializedName("id")
        val id: String? = "",

        @SerializedName("headline")
        val headline: Headline? = null
)