package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param

import com.google.gson.annotations.SerializedName

data class CreateUpdateStockReminderParam(
    @SerializedName("ShopID")
    val shopId: String = "",
    @SerializedName("IsSellerWh")
    val isSellerWh: Boolean = false,
    @SerializedName("ProductWarehouse")
    val productWareHouse: List<ProductWarehouseParam> = listOf()
)