package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName

data class TopadsInsightProductsResponse(
    @SerializedName("topadsInsightProducts")
    val topadsInsightProducts: TopadsInsightProducts? = null
) {
    data class TopadsInsightProducts(
        @SerializedName("data")
        val `data`: Data? = null,
        @SerializedName("errors")
        val errors: List<Any?>? = null
    ) {
        data class Data(
            @SerializedName("product")
            val product: List<Product?>? = null,
            @SerializedName("redirect_url")
            val redirectUrl: String? = null,
            @SerializedName("total_impression_loss")
            val totalImpressionLoss: String? = null
        ) {
            data class Product(
                @SerializedName("impression_loss")
                val impressionLoss: String? = null,
                @SerializedName("product_id")
                val productId: String? = null,
                @SerializedName("product_image")
                val productImage: String? = null,
                @SerializedName("status")
                val status: String? = null,
                @SerializedName("title")
                val title: String? = null
            )
        }
    }
}
