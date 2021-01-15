package com.tokopedia.discovery2.data


import com.google.gson.annotations.SerializedName

data class ComponentAdditionalInfo(
        @SerializedName("enabled")
        val enabled: Boolean,
        @SerializedName("total_product")
        val totalProductData: TotalProductData?,
)

data class TotalProductData(
        @SerializedName("product_count")
        val productCount: String? = null,
        @SerializedName("product_count_text")
        val productCountText: String? = null,
        @SerializedName("product_count_wording")
        val productCountWording: String? = null,
)