package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse

import com.google.gson.annotations.SerializedName

data class ProductWareHouse(
        @SerializedName("product_id")
        val productId: String,
        @SerializedName("warehouse_id")
        val wareHouseId: String,
        @SerializedName("stock")
        val stock: Int,
        @SerializedName("price")
        val price: Long,
        @SerializedName("threshold")
        val threshold: Int,
        @SerializedName("shop_id")
        val shopId: String
)