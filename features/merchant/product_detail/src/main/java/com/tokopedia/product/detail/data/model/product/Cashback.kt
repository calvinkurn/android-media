package com.tokopedia.product.detail.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cashback(
        @SerializedName("percentage")
        @Expose
        val percentage: Int = 0
)

data class PreOrder(
        @SerializedName("duration")
        @Expose
        val duration: Int = 0,

        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("timeUnit")
        @Expose
        val timeUnit: Int = 0
)

data class ReturnInfo(
        @SerializedName("colorHex")
        @Expose
        val colorHex: String = "",

        @SerializedName("colorRgb")
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
        @SerializedName("maxQty")
        @Expose
        val maxQty: Int = 0,

        @SerializedName("minQty")
        @Expose
        val minQty: Int = 0,

        @SerializedName("price")
        @Expose
        val price: Int = 0
)