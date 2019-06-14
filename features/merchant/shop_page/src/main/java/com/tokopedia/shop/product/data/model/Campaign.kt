package com.tokopedia.shop.product.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Campaign(
        @SerializedName("discounted_percentage")
        @Expose
        val discountedPercentage: String = "",

        @SerializedName("discounted_price")
        @Expose
        val discountedPrice: String = "",

        @SerializedName("discounted_price_fmt")
        @Expose
        val discountedPriceFmt: String = "",

        @SerializedName("original_price")
        @Expose
        val originalPrice: String = "",

        @SerializedName("original_price_fmt")
        @Expose
        val originalPriceFmt: String = ""
)