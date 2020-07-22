package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Gold(
        @SerializedName("isGold")
        @Expose
        val isGold: Int = 0,
        @SerializedName("badge")
        @Expose
        val badge: String = "",
        @SerializedName("badgeSVG")
        @Expose
        val badgeSvg: String = ""
)