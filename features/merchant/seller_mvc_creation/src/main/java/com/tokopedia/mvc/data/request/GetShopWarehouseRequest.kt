package com.tokopedia.mvc.data.request

import com.google.gson.annotations.SerializedName

data class GetShopWarehouseRequest(
    @SerializedName("shop_ids")
    val shopIds: List<Long>,
    @SerializedName("include_tc")
    val includeTc: Boolean = false,
    @SerializedName("warehouse_type")
    val warehouseType: Int = 0
)
