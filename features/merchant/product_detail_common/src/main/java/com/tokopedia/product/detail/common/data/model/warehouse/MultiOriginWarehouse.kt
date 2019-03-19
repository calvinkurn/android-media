package com.tokopedia.product.detail.common.data.model.warehouse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MultiOriginWarehouse(
        @SerializedName("product_id")
        @Expose
        val productId: String = "",

        @SerializedName("stock")
        @Expose
        val stock: Int = 0,

        @SerializedName("stock_wording")
        @Expose
        val stockWording: String = "",

        @SerializedName("price")
        @Expose
        val price: Int = 0,

        @SerializedName("warehouse_info")
        @Expose
        val warehouseInfo: WarehouseInfo = WarehouseInfo()
){

    data class Data(
            @SerializedName("data")
            @Expose
            val data: List<MultiOriginWarehouse> = listOf()
    )

    data class Response(
            @SerializedName("GetNearestWarehouse")
            @Expose
            val result: Data = Data()
    )
}