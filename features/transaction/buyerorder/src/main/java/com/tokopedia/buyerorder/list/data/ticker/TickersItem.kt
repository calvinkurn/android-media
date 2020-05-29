package com.tokopedia.buyerorder.list.data.ticker

import com.google.gson.annotations.SerializedName

data class TickersItem(

        @field:SerializedName("is_active")
        val isActive: Boolean,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("short_desc")
        val shortDesc: String? = null,

        @field:SerializedName("body")
        val body: String? = null
)