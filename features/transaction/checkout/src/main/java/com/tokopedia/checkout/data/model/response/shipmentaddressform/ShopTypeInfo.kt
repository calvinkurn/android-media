package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ShopTypeInfo(
        @SerializedName("shop_tier")
        val shopTier: Int = 0,
        @SerializedName("shop_grade")
        val shopGrade: Int = 0,
        @SerializedName("badge")
        val shopBadge: String = "",
        @SerializedName("badge_svg")
        val badgeSvg: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("title_fmt")
        val titleFmt: String = "" // Used for analytics `dimension81`
)