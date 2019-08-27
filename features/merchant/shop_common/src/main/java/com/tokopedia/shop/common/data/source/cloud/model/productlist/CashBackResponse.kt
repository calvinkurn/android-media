package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CashBackResponse(
        @SerializedName("cashback")
        @Expose
        val cashBack: Int = 0,

        @SerializedName("cashback_amount")
        @Expose
        val cashbackAmount: Int = 0
)