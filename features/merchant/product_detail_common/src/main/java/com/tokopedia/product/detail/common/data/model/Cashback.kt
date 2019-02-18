package com.tokopedia.product.detail.common.data.model

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
        val timeUnit: String = ""
)

data class Wholesale(

        @SerializedName("minQty")
        @Expose
        val minQty: Int = 0,

        @SerializedName("price")
        @Expose
        val price: Int = 0
)