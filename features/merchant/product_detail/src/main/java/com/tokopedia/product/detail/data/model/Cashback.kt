package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cashback(
        @SerializedName("percentage")
        @Expose
        val percentage: String = "",

        @SerializedName("value")
        @Expose
        val value: String = ""
)

data class PreOrder(
        @SerializedName("duration")
        @Expose
        val duration: Int = 0,

        @SerializedName("is_active")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("time_unit")
        @Expose
        val timeUnit: Int = 0
)

data class ReturnInfo(
        @SerializedName("color_hex")
        @Expose
        val colorHex: String = "",

        @SerializedName("color_rgb")
        @Expose
        val colorRgb: String = "",

        @SerializedName("content")
        @Expose
        val content: String = "",

        @SerializedName("icon")
        @Expose
        val icon: String = ""
)

data class Wholesale(
        @SerializedName("max_qty")
        @Expose
        val maxQty: Int = 0,

        @SerializedName("max_qty_fmt")
        @Expose
        val maxQtyFmt: String = "",

        @SerializedName("min_qty")
        @Expose
        val minQty: Int = 0,

        @SerializedName("min_qty_fmt")
        @Expose
        val minQtyFmt: String = "",

        @SerializedName("price")
        @Expose
        val price: Int = 0,

        @SerializedName("price_fmt")
        @Expose
        val priceFmt: String = ""
)