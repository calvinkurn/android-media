package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createresponse

import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("product_id")
        val productId: String,
        @SerializedName("warehouse_id")
        val wareHouseId: String,
        @SerializedName("threshold")
        val threshold: String
)