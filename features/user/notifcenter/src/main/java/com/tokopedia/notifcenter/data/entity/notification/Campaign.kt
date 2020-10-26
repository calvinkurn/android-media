package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class Campaign(
    @SerializedName("active")
    val active: Boolean = false,
    @SerializedName("discount_percentage")
    val discountPercentage: Int = 0,
    @SerializedName("discount_price")
    val discountPrice: Int = 0,
    @SerializedName("discount_price_fmt")
    val discountPriceFmt: String = "",
    @SerializedName("original_price")
    val originalPrice: Int = 0,
    @SerializedName("original_price_fmt")
    val originalPriceFmt: String = ""
)