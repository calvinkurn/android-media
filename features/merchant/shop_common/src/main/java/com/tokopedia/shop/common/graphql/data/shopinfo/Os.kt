package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Os (
        @SerializedName("isOfficial")
        @Expose
        val isOfficial: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("badge")
        @Expose
        val badge: String = "",
        @SerializedName("badgeSVG")
        @Expose
        val badgeSvg: String = ""
)