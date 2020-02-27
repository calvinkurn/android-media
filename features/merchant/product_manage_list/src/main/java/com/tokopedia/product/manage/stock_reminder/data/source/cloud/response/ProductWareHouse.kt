package com.tokopedia.product.manage.stock_reminder.data.source.cloud.response

import com.google.gson.annotations.SerializedName

data class ProductWareHouse(
        @SerializedName("product_id")
        val productId: String,
        @SerializedName("warehouse_id")
        val wareHouseId: String,
        @SerializedName("stock")
        val stock: Int,
        @SerializedName("price")
        val price: Long
)