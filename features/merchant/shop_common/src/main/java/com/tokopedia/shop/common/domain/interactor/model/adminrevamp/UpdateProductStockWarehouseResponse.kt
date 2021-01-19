package com.tokopedia.shop.common.domain.interactor.model.adminrevamp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class UpdateProductStockWarehouseResponse(
        @SerializedName("IMSUpdateProductWarehouse")
        @Expose
        val result: UpdateProductStockWarehouse? = UpdateProductStockWarehouse()
)

data class UpdateProductStockWarehouse(
        @SerializedName("header")
        @Expose
        val header: Header? = Header(),
        @SerializedName("data")
        @Expose
        val data: List<ProductStockWarehouse>? = listOf()
)

data class ProductStockWarehouse(
        @SerializedName("product_id")
        @Expose
        val productId: String? = "",
        @SerializedName("warehouse_id")
        @Expose
        val warehouseId: String? = "",
        @SerializedName("stock")
        @Expose
        val stock: String? = "",
        @SerializedName("shop_id")
        @Expose
        val shopId: String? = ""
)