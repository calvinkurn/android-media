package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class CouponFilterItem(
    @SerializedName("id")
    var id: Int = 0,
    @com.google.gson.annotations.SerializedName("name")
    var name: String = "",
    @SerializedName("isSelected")
    var isSelected:Boolean = false,
)
