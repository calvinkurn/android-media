package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param

import com.google.gson.annotations.SerializedName

data class ProductWarehouseParam(
    @SerializedName("ProductID")
    val productId: String = "",
    @SerializedName("WarehouseID")
    val wareHouseId: String = "",
    @SerializedName("Threshold")
    val threshold: String = ""
)
