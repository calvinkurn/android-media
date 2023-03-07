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
    val originalPriceFmt: String = "",

    @SerializedName("is_upcoming")
    @Expose
    val isUpcoming: Boolean = false,

    @SerializedName("stock_sold_percentage")
    @Expose
    val stockSoldPercentage: Float = 0f,

    @SerializedName("hide_gimmick")
    @Expose
    val hideGimmick: Boolean = false,

    @SerializedName("custom_stock")
    @Expose
    val customStock: String = "",

    @SerializedName("max_order")
    @Expose
    val maxOrder: Int = 0

)
