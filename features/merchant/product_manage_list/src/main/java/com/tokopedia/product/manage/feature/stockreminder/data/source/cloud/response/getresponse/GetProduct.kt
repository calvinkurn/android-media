package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse

import com.google.gson.annotations.SerializedName

data class GetProduct(
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("products_warehouse")
    val productsWareHouse: List<ProductWareHouse>
)