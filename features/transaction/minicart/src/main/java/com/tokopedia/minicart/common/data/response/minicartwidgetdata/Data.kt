package com.tokopedia.minicart.common.data.response.minicartwidgetdata

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("total_product_count")
        val totalProductCount: Int = 0,
        @SerializedName("total_product_error")
        val totalProductError: Int = 0,
        @SerializedName("total_product_price")
        val totalProductPrice: Long = 0
)