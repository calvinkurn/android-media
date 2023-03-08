package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class Warehouse(
    @SerializedName("warehouse_id")
    var warehouseId: String = ""
)
