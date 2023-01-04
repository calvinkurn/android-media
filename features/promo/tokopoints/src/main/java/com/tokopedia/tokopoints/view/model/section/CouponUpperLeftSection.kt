package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.SerializedName

data class CouponUpperLeftSection(
    @SerializedName("backgroundColor")
    var backgroundColor: String = "",
    @SerializedName("textAttributes")
    var textAttributes: List<TextAttributes> = listOf()
)
