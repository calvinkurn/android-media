package com.tokopedia.product.detail.common.data.model.pdplayout

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class Wholesale(

        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: WholesalePrice = WholesalePrice(),

        @SerializedName("minQty")
        val minQty: Int = 0
)

data class WholesalePrice(
        @SerializedName("value")
        val value: Double = 0.0
)