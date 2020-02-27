package com.tokopedia.product.manage.stock_reminder.data.source.cloud.response

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("products_warehouse")
    val productsWareHouse: List<ProductWareHouse>
)