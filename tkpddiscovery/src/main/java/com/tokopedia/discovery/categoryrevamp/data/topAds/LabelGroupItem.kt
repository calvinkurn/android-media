package com.tokopedia.discovery.categoryrevamp.data.topAds

import com.google.gson.annotations.SerializedName

data class LabelGroupItem(

        @field:SerializedName("position")
        val position: String? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("title")
        val title: String? = null
)