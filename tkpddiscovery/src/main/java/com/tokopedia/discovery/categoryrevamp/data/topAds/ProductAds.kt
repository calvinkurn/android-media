package com.tokopedia.discovery.categoryrevamp.data.topAds

import com.google.gson.annotations.SerializedName

data class ProductAds(

        @field:SerializedName("template")
        val template: List<Any?>? = null,

        @field:SerializedName("data")
        val data: List<DataItem?>? = null,

        @field:SerializedName("status")
        val status: Status? = null
)