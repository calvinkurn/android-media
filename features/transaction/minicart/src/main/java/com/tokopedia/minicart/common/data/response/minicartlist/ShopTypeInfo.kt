package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class ShopTypeInfo(
        @SerializedName("badge")
        val badge: String = "",
        @SerializedName("shop_grade")
        val shopGrade: Int = 0,
        @SerializedName("shop_tier")
        val shopTier: Int = 0,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("title_fmt")
        val titleFmt: String = ""
)