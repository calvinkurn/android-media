package com.tokopedia.vouchercreation.product.list.domain.model.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopLocGetWarehouseByShopIdsResponse(
        @SerializedName("ShopLocGetWarehouseByShopIDs")
        @Expose val ShopLocGetWarehouseByShopIDs: ShopLocGetWarehouseByShopIds
)

data class ShopLocGetWarehouseByShopIds(
        @SerializedName("warehouses")
        @Expose val warehouses: List<Warehouses>
)

@SuppressLint("Invalid Data Type")
data class Warehouses(
        @SerializedName("warehouse_id")
        @Expose val warehouseId: Int,
        @SerializedName("warehouse_name")
        @Expose val warehouseName: String,
        @SerializedName("warehouse_type")
        @Expose val warehouseType: Int,
)