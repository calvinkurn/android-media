package com.tokopedia.mvc.data.response


import com.google.gson.annotations.SerializedName

data class GetShopWarehouseResponse(
    @SerializedName("ShopLocGetWarehouseByShopIDs")
    val shopLocGetWarehouseByShopIDs: ShopLocGetWarehouseByShopIDs = ShopLocGetWarehouseByShopIDs()
) {
    data class ShopLocGetWarehouseByShopIDs(
        @SerializedName("warehouses")
        val warehouses: List<Warehouse> = listOf()
    ) {
        data class Warehouse(
            @SerializedName("warehouse_id")
            val warehouseId: String = "",
            @SerializedName("warehouse_name")
            val warehouseName: String = "",
            @SerializedName("warehouse_type")
            val warehouseType: Int = 0
        )
    }
}

